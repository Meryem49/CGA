package CGA.User.DataStructures.Geometry;

import org.joml.*;

public class Transformable implements ITransformable {

    private Matrix4f modelMatrix;
    private Transformable parent;

    public Transformable() {
        this.modelMatrix = new Matrix4f();
    }


    @Override
    public Matrix4f getLocalModelMatrix() {
        return modelMatrix;
    }

    /**
     * Rotates object around its own origin.
     *
     * @param pitch radiant angle around x-axis ccw
     * @param yaw   radiant angle around y-axis ccw
     * @param roll  radiant angle around z-axis ccw
     */

    @Override
    public void rotateLocal(float pitch, float yaw, float roll) {
        modelMatrix.rotateXYZ(pitch, yaw, roll);
    }

    /**
     * Rotates object around given rotation center.
     *
     * @param pitch       radiant angle around x-axis ccw
     * @param yaw         radiant angle around y-axis ccw
     * @param roll        radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */

    @Override
    public void rotateAroundPoint(float pitch, float yaw, float roll, Vector3f altMidpoint) {
        //falsch
        modelMatrix.translate(altMidpoint);
        modelMatrix.rotateXYZ(pitch,yaw,roll);
        modelMatrix.translate(altMidpoint.negate());
    }

    /**
     * Translates object based on its own coordinate system.
     *
     * @param deltaPos delta positions
     */

    @Override
    public void translateLocal(Vector3f deltaPos) {
        modelMatrix.translate(deltaPos);
    }

    /**
     * Scales object related to its own origin
     *
     * @param scale scale factor (x, y, z)
     */

    @Override
    public void scaleLocal(Vector3f scale) {
        modelMatrix.scale(scale);
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     *
     * @return position
     */

    @Override
    public Vector3f getPosition() {
        return new Vector3f(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32());
    }

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     *
     * @return x-axis
     */

    @Override
    public void setPosition(float x, float y, float z) {
        modelMatrix.setColumn(3, new Vector4f(x,y,z,0.0f));
    }

    @Override
    public Vector3f getXAxis() {
        Vector3f x = new Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02());
        return x.normalize();
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     *
     * @return y-axis
     */

    @Override
    public Vector3f getYAxis() {
        Vector3f y = new Vector3f(modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12());
        return y.normalize();
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     *
     * @return z-axis
     */

    @Override
    public Vector3f getZAxis() {
        Vector3f z = new Vector3f(modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22());
        return z.normalize();
    }


    // *****************************************
    // ********* needed for task 3.2.2 *********
    // ********* including scene-graph *********
    //  ****************************************

    /**
     * Set parent of current object
     *
     * @param parent parent node in scene graph
     */

    @Override
    public void setParent(Transformable parent) {
        this.parent = parent;
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     *
     * @return world modelMatrix
     */

    @Override
    public Matrix4f getWorldModelMatrix() {
        //bitte nochmal checken
        Matrix4f temp = new Matrix4f(modelMatrix);
        if (parent != null) {
            parent.getWorldModelMatrix().mul(modelMatrix, temp);
        }
        return temp;
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     *
     * @return position
     */

    @Override
    public Vector3f getWorldPosition() {
        Matrix4f temp = getWorldModelMatrix();
        return new Vector3f(temp.m30(), temp.m31(), temp.m32());
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     *
     * @return x-axis
     */

    @Override
    public Vector3f getWorldXAxis() {
        Matrix4f temp = getWorldModelMatrix();
        Vector3f x = new Vector3f(temp.m00(), temp.m01(), temp.m02());
        return x.normalize();
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     *
     * @return y-axis
     */

    @Override
    public Vector3f getWorldYAxis() {
        Matrix4f temp = getWorldModelMatrix();
        Vector3f y = new Vector3f(temp.m10(), temp.m11(), temp.m12());
        return y.normalize();
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     *
     * @return z-axis
     */

    @Override
    public Vector3f getWorldZAxis() {
        Matrix4f temp = getWorldModelMatrix();
        Vector3f z = new Vector3f(temp.m20(), temp.m21(), temp.m22());
        return z.normalize();
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     *
     * @param deltaPos delta positions (x, y, z)
     */

    @Override
    public void translateGlobal(Vector3f deltaPos) {
        Matrix4f temp = new Matrix4f().translate(deltaPos);
        modelMatrix = temp.mul(modelMatrix);

    }


}
