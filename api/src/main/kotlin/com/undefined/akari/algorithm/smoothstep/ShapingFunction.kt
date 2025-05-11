package com.undefined.akari.algorithm.smoothstep

import com.undefined.akari.algorithm.smoothstep.SmoothStepAlgorithm.MathUtils.lerp

enum class ShapingFunction {

    SQUARED {
        override fun apply(t: Float): Float {
            return t * t
        }
    },
    SMOOTHSTEP { // In & Out smoothing
        override fun apply(t: Float): Float {
            val sqr = SQUARED.apply(t)
            val quadOut = QUADRATIC_EASE_OUT.apply(t)
            return lerp(sqr, quadOut, t) // Please help I suck at extension functions
        }
    },
    QUADRATIC_EASE_OUT {
        override fun apply(t: Float): Float {
            return 1.0f - (1.0f - t) * (1.0f - t)
        }
    },
    PARAMETRIC_BLEND {
        override fun apply(t: Float): Float {
            val sqr = t * t;
            return sqr / (2.0f * (sqr - t) + 1.0f)
        }
    }
    ;

    abstract fun apply(t: Float): Float
}
