package lejos.robotics.objectdetection;

import lejos.robotics.RangeFinder;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/*
 * DEVELOPER NOTES:
 * TODO: Some of these API docs might be more appropriate in FeatureDetector
 * TODO: One weakness of this class is it forces RangeFinder to use multiple readings. Might be some benefits to only
 * retrieving one reading from ultrasonic sensor.
 */

/**
 * <p>The RangeFeatureDetector used a RangeFinder to locate objects (known as features when mapping). This class is
 * unable to identify the feature and merely reports the range and angle to the object.</p>
 * 
 *  <p>You can also have the scan identify the object (such as a camera using facial recognition to identify a person).
 *  One possibility to implement this is to extend RangeFeatureDetector and add a camera to the constructor. When the
 *  FeatureDetector range scanner detects an object, take a picture and look for a face in the image. This is then
 *  reported as an extended class of RangeFeature called PersonFeature, which has a getPerson() method containing 
 *  information on the object that was detected.</p>
 *  
 * <p>To create a more complex FeatureDetector, extend this class and override the {@link FeatureDetector#scan()} method.
 * It is possible to add more complex functionality in this method, such as only returning a "hit" if the scanner detects
 * an object in the same location twice in a row. This type of filtering could also take place in the FeatureListener
 * method, although then single {@link FeatureDetector#scan()} calls will not contain the filtering.</p>
 * 
 *  
 * @author BB based on concepts by Lawrie Griffiths
 *
 */
public class RangeFeatureDetector extends FeatureDetectorAdapter {
	
	private RangeFinder range_finder = null;
	private float max_dist = 100;
	
	// TODO: Accept optional RangeScanner?
	
	// TODO: Alternate constructor for range sensors angled and mounted non-center.
	
	/**
	 * If a range finder is used, assumes object is detected straight ahead so heading is 
	 * always 0 for the returned RangeReading. 
	 * @param rf The range finder sensor. e.g. UltrasonicSensor
	 * @param maxDistance The upper limit of distance it will report. e.g. 40 cm.
	 * @param delay The interval range finder checks for objects. e.g. 250 ms.
	 * @see lejos.nxt.UltrasonicSensor
	 */
	public RangeFeatureDetector(RangeFinder rf, float maxDistance, int delay) {
		super(delay);
		this.range_finder = rf;
		setMaxDistance(maxDistance);
	}

	/**
	 * Sets the maximum distance to register detected objects from the range finder.
	 * @param distance The maximum distance. e.g. 40 cm.
	 */
	public void setMaxDistance(float distance) {
		this.max_dist = distance;
	}
	
	/**
	 * Returns the maximum distance the FeatureDetector will return for detected objects. 
	 * @return The maximum distance. e.g. 40 cm.
	 */
	public float getMaxDistance(){
		return this.max_dist;
	}
	
	@Override
	public Feature scan() {
		RangeFeature feature = null;
		// TODO: Note: If it is slower to retrieve multiple rather than single scan. Have option for single only in constructor?
		float [] ranges = range_finder.getRanges();
		RangeReadings rrs = new RangeReadings(0); // TODO: Should it omit anything outside max_dist?
		if(ranges.length <= 0) return null; // Check to make sure it retrieved some readings. Seems to return nothing sometimes. 
		if(ranges[0] > 0 & ranges[0] < max_dist) { 
			for(int i=0;i<ranges.length;i++) {
				int angle = 0; // TODO: Activate global "angle" setting for angle sensor is mounted at on vehicle. Constructor param.
				rrs.add(new RangeReading(angle, ranges[i]));
			}
			feature = new RangeFeature(rrs);
		}
		return feature;
	}	
}