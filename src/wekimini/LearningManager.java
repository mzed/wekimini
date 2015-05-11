/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import wekimini.osc.OSCOutput;
import wekimini.util.WeakListenerSupport;

/**
 *
 * @author rebecca
 */
public class LearningManager {
    public static enum LearningState {TRAINING, NOT_TRAINING, NOT_READY_TO_TRAIN};
    public static enum RunningState {RUNNING, NOT_RUNNING};
    public static enum RecordingState {RECORDING, NOT_RECORDING};
    //private RunningState runningState = RunningState.NOT_RUNNING;
    private final List<Path> paths = new ArrayList<>(5);
    private final Wekinator w;
    private final WeakListenerSupport wls = new WeakListenerSupport();
    private boolean[] pathRecordingMask;
    private boolean[] pathRunningMask;
    private double[] myComputedOutputs;
    private int trainingRound = 1;
    private int recordingRound = 1;
    
    public static final String PROP_LEARNINGSTATE = "learningState";
    private LearningState learningState = LearningState.NOT_READY_TO_TRAIN;

    private RunningState runningState = RunningState.NOT_RUNNING;
    public static final String PROP_RUNNINGSTATE = "runningState";
    
    private RecordingState recordingState = RecordingState.NOT_RECORDING;
    public static final String PROP_RECORDINGSTATE = "recordingState";

    /**
     * Get the value of recordingState
     *
     * @return the value of recordingState
     */
    public RecordingState getRecordingState() {
        return recordingState;
    }

    /**
     * Set the value of recordingState
     *
     * @param recordingState new value of recordingState
     */
    public void setRecordingState(RecordingState recordingState) {
        RecordingState oldRecordingState = this.recordingState;
        this.recordingState = recordingState;
        propertyChangeSupport.firePropertyChange(PROP_RECORDINGSTATE, oldRecordingState, recordingState);
    }

    /**
     * Get the value of runningState
     *
     * @return the value of runningState
     */
    public RunningState getRunningState() {
        return runningState;
    }

    /**
     * Set the value of runningState
     *
     * @param runningState new value of runningState
     */
    public void setRunningState(RunningState runningState) {
        RunningState oldRunningState = this.runningState;
        this.runningState = runningState;
        propertyChangeSupport.firePropertyChange(PROP_RUNNINGSTATE, oldRunningState, runningState);
    }

    /**
     * Get the value of learningState
     *
     * @return the value of learningState
     */
    public LearningState getLearningState() {
        return learningState;
    }

    /**
     * Set the value of learningState
     *
     * @param learningState new value of learningState
     */
    private void setLearningState(LearningState learningState) {
        LearningState oldLearningState = this.learningState;
        this.learningState = learningState;
        propertyChangeSupport.firePropertyChange(PROP_LEARNINGSTATE, oldLearningState, learningState);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    
    public LearningManager(Wekinator w) {
        this.w = w;
        
        //TODO listen for changes in input names, # outputs or inputs, etc.
        w.getInputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                inputGroupChanged(evt);
            }
        });
        w.getOutputManager().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == OutputManager.PROP_OUTPUTGROUP) {
                    outputGroupChanged(evt);
                }
            }
        });    
    }
    
    //For now, assume only 1 path per output is allowed in system
    
    public List<Path> getPaths() {
        return paths;
    }
    
    //Call when both Input and Output Managers are ready
    public void initializeInputsAndOutputs() {
        String[] inputNames = w.getInputManager().getInputNames();
        int numOutputs = w.getOutputManager().getOutputGroup().getNumOutputs();
        pathRecordingMask = new boolean[numOutputs];
        pathRunningMask = new boolean[numOutputs];
        myComputedOutputs = new double[numOutputs];
        
        for (int i = 0; i < numOutputs; i++) {
            pathRecordingMask[i] = true;
            pathRunningMask[i] = true;
            
            OSCOutput o = w.getOutputManager().getOutputGroup().getOutput(i);
            Path p = new Path(o, inputNames, w);
            PropertyChangeListener pChange = new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    pathChanged(p, evt);
                }
            };
            p.addPropertyChangeListener(wls.propertyChange(pChange));
            paths.add(p);
        }  
        setLearningState(LearningState.NOT_TRAINING);
    }
    
    private void pathChanged(Path p, PropertyChangeEvent evt) {
        //if (evt.getPropertyName() == Path.)
        //TODO: Listen for path input selection change in order to change our Filter
        //TODO: listen for record/run enable change and update our Mask
        System.out.println("Path changed for output: " + p.getOSCOutput().getName());
        
    }
    
    //Right now, this simply won't change indices where mask is false
    public double[] computeValues(double[] inputs, boolean[] computeMask) {
        for (int i = 0; i < computeMask.length; i++) {
            if (computeMask[i]) {
                myComputedOutputs[i] = paths.get(i).compute(inputs);
            }
        }
        return myComputedOutputs;
    }
    
    public void addToTraining(double[] inputs, double[] outputs, boolean[] recordingMask) {
        /*double[] trainingOutputs = new double[outputs.length];
        
        for (int i = 0; i < outputs.length; i++) {
            if (recordingMask[i]) {
                
            }
        } */
        DataManager.addToTraining(inputs, outputs, recordingMask, trainingRound);
    } 
    
    //TODO: Need to do this in background and change training state
    public void buildModels(boolean trainMask[]) {
        for (int i = 0; i < trainMask.length; i++) {
            if (trainMask[i]) {
                paths.get(i).buildModel(paths.get(i).getOSCOutput().getName() + "-" + trainingRound);
            }
        }
        trainingRound++;
    }
    
    public void updateInputs(double[] inputs) {
        if (recordingState == RecordingState.RECORDING) {
            addToTraining(inputs, w.getOutputManager().getCurrentValues(), pathRecordingMask);
        } else if (runningState == RunningState.RUNNING) {
            double[] d = computeValues(inputs, pathRunningMask);
            w.getOutputManager().setNewComputedValues(d);
        }
    }
    
    private void outputGroupChanged(PropertyChangeEvent evt) {
        System.out.println("ERROR: LearningManager doesn't know how to handle this");
    }
    
    private void inputGroupChanged(PropertyChangeEvent evt) {
        System.out.println("ERROR: LearningManager doesn't know how to handle this");
    }
    
}