package com.darklabs.circularview

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.Dimension
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.*


class CircularLayoutManager : LinearLayoutManager {

    @Dimension
    private var mRadius: Int = MIN_RADIUS

    @Gravity
    private var mGravity: Int = Gravity.START

    @Dimension
    private var mPeekDistance: Int = MIN_PEEK

    private var mIsRotating: Boolean = false

    private var mCenter: Point

    /**
     * @param gravity      The {@link Gravity} that will define where the anchor point is for this layout manager.  The
     *                     gravity point is the point around which items orbit.
     * @param orientation  The orientation as defined in [RecyclerView], and enforced by [Orientation]
     * @param radius       The radius of the rotation angle, which helps define the curvature of the turn.  This value
     *                     will be clamped to {@code [0, MAX_INT]} inclusive.
     * @param peekDistance The absolute extra distance from the {@link Gravity} edge after which this layout manager will start
     *                     placing items.  This value will be clamped to {@code [0, radius]} inclusive.
     * @param isRotating   Should the items rotate as if on a turning surface, or should they maintain
     *                     their angle with respect to the screen as they orbit the center point?
     */
    constructor(context: Context?,
                @Gravity gravity: Int = Gravity.START,
                @Orientation orientation: Int = Orientation.VERTICAL,
                @Dimension radius: Int = DEFAULT_RADIUS,
                @Dimension peekDistance: Int = DEFAULT_PEEK,
                isRotating: Boolean = false) : super(context, orientation, false) {
        this.mRadius = max(radius, MIN_RADIUS)
        this.mGravity = gravity
        this.mPeekDistance = min(max(peekDistance, MIN_PEEK), radius)
        this.mIsRotating = isRotating
        mCenter = Point()
    }


    constructor(context: Context,
                attr: AttributeSet,
                defStyleAttr: Int,
                defStyleRes: Int
    ) : super(context, attr, defStyleAttr, defStyleRes) {

        with(context.obtainStyledAttributes(
                attr,
                R.styleable.RecyclerView,
                defStyleAttr,
                defStyleRes)) {
            super.setOrientation(createOrientationFromAttr(R.styleable.RecyclerView_orientation))
            setGravity(createGravityFromAttr(R.styleable.RecyclerView_gravity))
            setRadius(getInt(R.styleable.RecyclerView_radius, DEFAULT_RADIUS))
            setPeekDistance(getInt(R.styleable.RecyclerView_peekDistance, DEFAULT_PEEK))
            setRotating(getBoolean(R.styleable.RecyclerView_isRotating, false))
            mCenter = Point()
            recycle()
        }
    }

    fun setRadius(radius: Int) {
        this.mRadius = max(radius, MIN_RADIUS)
        requestLayout()
    }

    fun setPeekDistance(peekDistance: Int) {
        this.mPeekDistance = min(max(peekDistance, MIN_PEEK), mRadius)
        requestLayout()
    }

    fun setGravity(@Gravity gravity: Int) {
        this.mGravity = gravity
        requestLayout()
    }

    fun setRotating(rotate: Boolean) {
        this.mIsRotating = rotate
        requestLayout()
    }

    override fun scrollVerticallyBy(
            dy: Int,
            recycler: Recycler,
            state: RecyclerView.State): Int {
        val by = super.scrollVerticallyBy(dy, recycler, state)
        setChildOffsetsVertical(mGravity, mRadius, mCenter, mPeekDistance)
        return by
    }

    override fun scrollHorizontallyBy(
            dx: Int,
            recycler: Recycler,
            state: RecyclerView.State): Int {
        val by = super.scrollHorizontallyBy(dx, recycler, state)
        setChildOffsetsHorizontal(mGravity, mRadius, mCenter, mPeekDistance)
        return by
    }

    override fun onLayoutChildren(
            recycler: Recycler,
            state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        mCenter = deriveCenter(mGravity, orientation, mRadius, mPeekDistance, mCenter)
        setChildOffsets(mGravity, orientation, mRadius, mCenter, mPeekDistance)
    }

    /**
     * Accounting for the settings of [Gravity] and [Orientation], find the center point
     * around which this layout manager should arrange list items.  Place the resulting coordinates
     * into `out`, to avoid reallocation.
     */
    private fun deriveCenter(@Gravity gravity: Int,
                             orientation: Int,
                             @Dimension radius: Int,
                             @Dimension peekDistance: Int,
                             out: Point): Point {

        val gravitySign = if (gravity == Gravity.START) -1 else 1
        val distanceMultiplier = if (gravity == Gravity.START) 0 else 1

        val (x, y) = when (orientation) {

            Orientation.HORIZONTAL ->
                Pair(width / 2,
                        distanceMultiplier * height + gravitySign * abs(radius - peekDistance))

            Orientation.VERTICAL ->
                Pair(distanceMultiplier * width + gravitySign * abs(radius - peekDistance),
                        height / 2)

            else ->
                Pair(distanceMultiplier * width + gravitySign * abs(radius - peekDistance),
                        height / 2)

        }

        out[x] = y
        return out
    }

    /**
     * Find the absolute horizontal distance by which a view at `viewY` should offset
     * to align with the circle `center` with `radius`, accounting for `peekDistance`.
     */
    private fun resolveOffsetX(radius: Double,
                               viewY: Double,
                               center: Point,
                               peekDistance: Int): Double {
        val opposite = abs(center.y - viewY)
        val radiusSquared = radius * radius
        val oppositeSquared = opposite * opposite
        val adjacentSideLength = sqrt(radiusSquared - oppositeSquared)
        return adjacentSideLength - radius + peekDistance
    }

    /**
     * Find the absolute vertical distance by which a view at `viewX` should offset to
     * align with the circle `center` with `radius`, account for `peekDistance`.
     */
    private fun resolveOffsetY(radius: Double,
                               viewX: Double,
                               center: Point,
                               peekDistance: Int): Double {
        val adjacent = abs(center.x - viewX)
        val radiusSquared = radius * radius
        val adjacentSquared = adjacent * adjacent
        val oppositeSideLength = sqrt(radiusSquared - adjacentSquared)
        return oppositeSideLength - radius + peekDistance
    }

    /**
     * Traffic method to divert calls based on [Orientation].
     *
     * @see .setChildOffsetsVertical
     * @see .setChildOffsetsHorizontal
     */
    private fun setChildOffsets(@Gravity gravity: Int,
                                orientation: Int,
                                @Dimension radius: Int,
                                center: Point,
                                peekDistance: Int) {
        if (orientation == VERTICAL) {
            setChildOffsetsVertical(gravity, radius, center, peekDistance)
        } else if (orientation == HORIZONTAL) {
            setChildOffsetsHorizontal(gravity, radius, center, peekDistance)
        }
    }

    /**
     * Set the bumper offsets on child views for [Orientation.VERTICAL]
     */
    private fun setChildOffsetsVertical(@Gravity gravity: Int,
                                        @Dimension radius: Int,
                                        center: Point,
                                        peekDistance: Int) {

        (0 until childCount).forEach { i ->
            val child = getChildAt(i)
            val layoutParams = child?.layoutParams as RecyclerView.LayoutParams
            val xOffset = resolveOffsetX(
                    radius.toDouble(),
                    child.y + child.height / 2.0f.toDouble(),
                    center,
                    peekDistance).toInt()
            val x = if (gravity == Gravity.START) {
                xOffset + getMarginStart(layoutParams)
            } else {
                width - xOffset - child.width - getMarginStart(layoutParams)
            }
            child.layout(x, child.top, child.width + x, child.bottom)
            setChildRotationVertical(gravity, child, radius, center)
        }
    }

    /**
     * Given that the is [Orientation.VERTICAL], apply rotation if rotation is enabled.
     */
    private fun setChildRotationVertical(@Gravity gravity: Int,
                                         child: View?,
                                         radius: Int,
                                         center: Point) {

        if (mIsRotating.not()) {
            child?.rotation = 0f
            return
        }
        val childPastCenter = (child?.y?.plus((child.height / 2)))!! > center.y
        val directionMult: Float = if (gravity == Gravity.END) {
            (if (childPastCenter) -1 else 1).toFloat()
        } else {
            (if (childPastCenter) 1 else -1).toFloat()
        }
        val opposite = abs(child.y + child.height / 2.0f - center.y)
        child.rotation = (directionMult * Math.toDegrees(asin(opposite / radius.toDouble()))).toFloat()
    }

    /**
     * Set bumper offsets on child views for [Orientation.HORIZONTAL]
     */
    private fun setChildOffsetsHorizontal(@Gravity gravity: Int,
                                          @Dimension radius: Int,
                                          center: Point,
                                          peekDistance: Int) {

        (0 until childCount).forEach { i ->
            val child = getChildAt(i)
            val layoutParams = child?.layoutParams as RecyclerView.LayoutParams
            val yOffset = resolveOffsetY(radius.toDouble(),
                    child.x + child.width / 2.0f.toDouble(),
                    center,
                    peekDistance).toInt()

            val y = if (gravity == Gravity.START) {
                yOffset + getMarginStart(layoutParams)
            } else {
                height - yOffset - child.height - getMarginStart(layoutParams)
            }
            child.layout(child.left, y, child.right, child.height + y)
            setChildRotationHorizontal(gravity, child, radius, center)
        }
    }

    /**
     * Given that the orientation is [Orientation.HORIZONTAL], apply rotation if enabled.
     */
    private fun setChildRotationHorizontal(@Gravity gravity: Int,
                                           child: View?,
                                           radius: Int,
                                           center: Point) {
        if (mIsRotating.not()) {
            child?.rotation = 0f
            return
        }

        val childPastCenter = child!!.x + child.width / 2 > center.x
        val directionMult: Float = if (gravity == Gravity.END) {
            (if (childPastCenter) 1 else -1).toFloat()
        } else {
            (if (childPastCenter) -1 else 1).toFloat()
        }
        val opposite = abs(child.x + child.width / 2.0f - center.x)
        child.rotation = (directionMult * Math.toDegrees(asin(opposite / radius.toDouble()))).toFloat()
    }

    /**
     * @see android.view.ViewGroup.MarginLayoutParams.getMarginStart
     */
    private fun getMarginStart(layoutParams: MarginLayoutParams): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.marginStart
        } else {
            layoutParams.leftMargin
        }
    }
}