package CGA.User.DataStructures.Geometry;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface ITransformable {

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    public Matrix4f getLocalModelMatrix();

    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */
    public void rotateLocal(float pitch, float yaw, float roll);

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    public void rotateAroundPoint(float pitch, float yaw, float roll, Vector3f altMidpoint);

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    public void translateLocal(Vector3f deltaPos);

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    public void scaleLocal(Vector3f scale);

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    public Vector3f getPosition() ;

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */

    public void setPosition(float x,float y, float z) ;

    public Vector3f getXAxis() ;

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    public Vector3f getYAxis() ;

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    public Vector3f getZAxis() ;


    // *****************************************
    // ********* needed for task 3.2.2 *********
    // ********* including scene-graph *********
    //  ****************************************

    /**
     * Set parent of current object
     * @param parent parent node in scene graph
     */
    public void setParent(Transformable parent);

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    public Matrix4f getWorldModelMatrix();

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    public Vector3f getWorldPosition() ;

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    public Vector3f getWorldXAxis();

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    public Vector3f getWorldYAxis();

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    public Vector3f getWorldZAxis() ;

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    public void translateGlobal(Vector3f deltaPos);

}
