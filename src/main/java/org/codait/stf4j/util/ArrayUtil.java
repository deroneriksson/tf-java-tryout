// ------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ------------------------------------------------------------------------

package org.codait.stf4j.util;

import static org.codait.stf4j.util.TypeUtil.*;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codait.stf4j.TFException;
import org.tensorflow.Tensor;
import org.tensorflow.types.UInt8;

/**
 * Utility class for working with Arrays related to TensorFlow.
 *
 */
public class ArrayUtil {

	/**
	 * Logger for ArrayUtil
	 */
	protected static Logger log = LogManager.getLogger(ArrayUtil.class);

	/**
	 * Convert {@code Tensor<Boolean>} to boolean array.
	 * 
	 * @param tensor
	 *            The Tensor of Boolean values
	 * @return Primitive boolean array
	 */
	public static boolean[] booleanTensorToBooleanArray(Tensor<Boolean> tensor) {
		ByteBuffer bb = ByteBuffer.allocate(tensor.numElements());
		tensor.writeTo(bb);
		byte[] byteArray = bb.array();
		boolean[] booleanArray = new boolean[byteArray.length];
		for (int i = 0; i < byteArray.length; i++) {
			booleanArray[i] = byte_to_boolean(byteArray[i]);
		}
		return booleanArray;
	}

	/**
	 * Convert {@code Tensor<Boolean>} to byte array.
	 * 
	 * @param tensor
	 *            The Tensor of Boolean values
	 * @return Primitive byte array
	 */
	public static byte[] booleanTensorToByteArray(Tensor<Boolean> tensor) {
		ByteBuffer bb = ByteBuffer.allocate(tensor.numElements());
		tensor.writeTo(bb);
		return bb.array();
	}

	/**
	 * Convert {@code Tensor<Boolean>} to multidimensional boolean array.
	 * 
	 * @param tensor
	 *            The Tensor of Boolean values
	 * @return Multidimensional primitive boolean array as an Object
	 */
	public static Object booleanTensorToMultidimensionalBooleanArray(Tensor<Boolean> tensor) {
		int[] shape = lToI(tensor.shape());
		Object b = Array.newInstance(boolean.class, shape);
		tensor.copyTo(b);
		return b;
	}

	/**
	 * Convert individual 2d int arrays with same dimensions to a single 3d int array.
	 * 
	 * @param arrays
	 *            2d int arrays of the same dimensions
	 * @return 3d int array that combines the individual 2d int arrays
	 */
	public static int[][][] convert2dIntArraysTo3dIntArray(int[][]... arrays) {
		int[][][] array = new int[arrays.length][arrays[0].length][arrays[0][0].length];
		for (int i = 0; i < arrays.length; i++) {
			int[][] ar = arrays[i];
			for (int r = 0; r < ar.length; r++) {
				for (int c = 0; c < ar[0].length; c++) {
					array[i][r][c] = ar[r][c];
				}
			}
		}
		return array;
	}

	/**
	 * Convert an array from one type to another, where the destination type is specified by the destType parameter.
	 * 
	 * @param orig
	 *            The original array
	 * @param destType
	 *            The type (class) that the original array should be converted to
	 * @return The resulting array
	 */
	public static Object convertArrayType(Object orig, Class<?> destType) {
		int[] dimensions = getArrayDimensions(orig);
		Object dest = Array.newInstance(destType, dimensions);
		copyArrayVals(orig, dest);
		return dest;
	}

	/**
	 * Convert an unsigned array from one type to another, where the destination type is specified by the destType
	 * parameter.
	 * 
	 * @param orig
	 *            The original array of unsigned values
	 * @param destType
	 *            The type (class) that the original array should be converted to
	 * @return The resulting array
	 */
	public static Object convertUnsignedArrayType(Object orig, Class<?> destType) {
		int[] dimensions = getArrayDimensions(orig);
		Object dest = Array.newInstance(destType, dimensions);
		copyUnsignedArrayVals(orig, dest);
		return dest;
	}

	/**
	 * Copy values from one array to another array with the same shape and perform needed type conversions.
	 * 
	 * @param orig
	 *            The original array
	 * @param dest
	 *            The destination array
	 */
	public static void copyArrayVals(Object orig, Object dest) {
		String o = orig.getClass().getComponentType().getSimpleName();
		String d = dest.getClass().getComponentType().getSimpleName();
		for (int i = 0; i < Array.getLength(orig); i++) {
			Object v = Array.get(orig, i);
			Object vd = Array.get(dest, i);
			try {
				if (v.getClass().isArray()) {
					copyArrayVals(v, vd);
				} else {
					if ("boolean".equalsIgnoreCase(o)) {
						if ("byte".equalsIgnoreCase(d)) {
							Array.set(dest, i, boolean_to_byte((boolean) v));
						} else if ("double".equalsIgnoreCase(d)) {
							Array.set(dest, i, boolean_to_double((boolean) v));
						} else if ("float".equalsIgnoreCase(d)) {
							Array.set(dest, i, boolean_to_float((boolean) v));
						} else if ("int".equals(d) || "Integer".equals(d)) {
							Array.set(dest, i, boolean_to_int((boolean) v));
						} else if ("long".equalsIgnoreCase(d)) {
							Array.set(dest, i, boolean_to_long((boolean) v));
						} else if ("String".equals(d)) {
							Array.set(dest, i, boolean_to_String((boolean) v));
						} else {
							Array.set(dest, i, v);
						}
					} else if ("double".equalsIgnoreCase(o)) {
						if ("boolean".equalsIgnoreCase(d)) {
							Array.set(dest, i, double_to_boolean((double) v));
						} else if ("byte".equalsIgnoreCase(d)) {
							Array.set(dest, i, double_to_byte((double) v));
						} else if ("float".equalsIgnoreCase(d)) {
							Array.set(dest, i, double_to_float((double) v));
						} else if ("int".equals(d) || "Integer".equals(d)) {
							Array.set(dest, i, double_to_int((double) v));
						} else if ("long".equalsIgnoreCase(d)) {
							Array.set(dest, i, double_to_long((double) v));
						} else if ("String".equals(d)) {
							Array.set(dest, i, double_to_String((double) v));
						} else {
							Array.set(dest, i, v);
						}
					} else if ("float".equalsIgnoreCase(o)) {
						if ("boolean".equalsIgnoreCase(d)) {
							Array.set(dest, i, float_to_boolean((float) v));
						} else if ("byte".equalsIgnoreCase(d)) {
							Array.set(dest, i, float_to_byte((float) v));
						} else if ("double".equalsIgnoreCase(d)) {
							Array.set(dest, i, float_to_double((float) v));
						} else if ("int".equals(d) || "Integer".equals(d)) {
							Array.set(dest, i, float_to_int((float) v));
						} else if ("long".equalsIgnoreCase(d)) {
							Array.set(dest, i, float_to_long((float) v));
						} else if ("String".equals(d)) {
							Array.set(dest, i, float_to_String((float) v));
						} else {
							Array.set(dest, i, v);
						}
					} else if ("int".equals(o) || "Integer".equals(o)) {
						if ("boolean".equalsIgnoreCase(d)) {
							Array.set(dest, i, int_to_boolean((int) v));
						} else if ("byte".equalsIgnoreCase(d)) {
							Array.set(dest, i, int_to_byte((int) v));
						} else if ("double".equalsIgnoreCase(d)) {
							Array.set(dest, i, int_to_double((int) v));
						} else if ("float".equalsIgnoreCase(d)) {
							Array.set(dest, i, int_to_float((int) v));
						} else if ("long".equalsIgnoreCase(d)) {
							Array.set(dest, i, int_to_long((int) v));
						} else if ("String".equals(d)) {
							Array.set(dest, i, int_to_String((int) v));
						} else {
							Array.set(dest, i, v);
						}
					} else if ("long".equalsIgnoreCase(o)) {
						if ("boolean".equalsIgnoreCase(d)) {
							Array.set(dest, i, long_to_boolean((long) v));
						} else if ("byte".equalsIgnoreCase(d)) {
							Array.set(dest, i, long_to_byte((long) v));
						} else if ("double".equalsIgnoreCase(d)) {
							Array.set(dest, i, long_to_double((long) v));
						} else if ("float".equalsIgnoreCase(d)) {
							Array.set(dest, i, long_to_float((long) v));
						} else if ("int".equals(d) || "Integer".equals(d)) {
							Array.set(dest, i, long_to_int((long) v));
						} else if ("String".equals(d)) {
							Array.set(dest, i, long_to_String((long) v));
						} else {
							Array.set(dest, i, v);
						}
					} else if ("String".equals(o)) {
						if ("boolean".equalsIgnoreCase(d)) {
							Array.set(dest, i, String_to_boolean((String) v));
						} else if ("byte".equalsIgnoreCase(d)) {
							Array.set(dest, i, String_to_byte((String) v));
						} else if ("double".equalsIgnoreCase(d)) {
							Array.set(dest, i, String_to_double((String) v));
						} else if ("float".equalsIgnoreCase(d)) {
							Array.set(dest, i, String_to_float((String) v));
						} else if ("int".equals(d) || "Integer".equals(d)) {
							Array.set(dest, i, String_to_int((String) v));
						} else if ("long".equalsIgnoreCase(d)) {
							Array.set(dest, i, String_to_long((String) v));
						} else {
							Array.set(dest, i, v);
						}
					} else if ("byte".equalsIgnoreCase(o) && "boolean".equalsIgnoreCase(d)) {
						Array.set(dest, i, byte_to_boolean((byte) v));
					} else {
						Array.set(dest, i, v);
					}
				}
			} catch (Exception e) {
				throw new TFException("Problem converting array values ('" + o + "' to '" + d + "' with value '" + v
						+ "'): " + e.getMessage(), e);
			}
		}

	}

	/**
	 * Convert byte values in a multidimensional (dim) array to String values in a multidimensional (dim-1) array. The
	 * multidimensional byte array has 1 more dimension than the multidimensional String array since a 1D byte array is
	 * converted to a String.
	 * 
	 * @param orig
	 *            The multidimensional (dim) byte array
	 * @param dest
	 *            The multidimensional (dim-1) String array
	 * @param dimCount
	 *            String dimension count for recursion. If greater than one, recurse through multidimensional byte
	 *            array. Otherwise, copy 1D byte array to corresponding String and place in multidimensional String
	 *            array.
	 */
	protected static void copyByteArrayToStringArrayVals(Object orig, Object dest, int dimCount) {
		for (int i = 0; i < Array.getLength(orig); i++) {
			Object v = Array.get(orig, i);
			Object vd = Array.get(dest, i);
			if (dimCount > 1) {
				int newDim = dimCount - 1;
				copyByteArrayToStringArrayVals(v, vd, newDim);
			} else {
				String s = new String((byte[]) v, StandardCharsets.UTF_8);
				Array.set(dest, i, s);
			}
		}
	}

	/**
	 * Convert String values in a multidimensional (dim) array to byte values in a multidimensional (dim+1) array. The
	 * multidimensional byte array has 1 more dimension than the multidimensional String array since a String is
	 * converted to a byte array.
	 * 
	 * @param orig
	 *            The multidimensional (dim) String array
	 * @param dest
	 *            The multidimensional (dim+1) byte array
	 */
	protected static void copyStringArrayToByteArrayVals(Object orig, Object dest) {
		for (int i = 0; i < Array.getLength(orig); i++) {
			Object v = Array.get(orig, i);
			Object vd = Array.get(dest, i);
			if (v.getClass().isArray()) {
				copyStringArrayToByteArrayVals(v, vd);
			} else {
				String s = (String) v;
				byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
				Array.set(dest, i, bytes);
			}
		}
	}

	/**
	 * Copy unsigned values from one array to another array with the same shape and perform needed type conversions.
	 * 
	 * @param orig
	 *            The original array of unsigned values
	 * @param dest
	 *            The destination array
	 */
	public static void copyUnsignedArrayVals(Object orig, Object dest) {
		String o = orig.getClass().getComponentType().getSimpleName();
		String d = dest.getClass().getComponentType().getSimpleName();
		for (int i = 0; i < Array.getLength(orig); i++) {
			Object v = Array.get(orig, i);
			Object vd = Array.get(dest, i);
			try {
				if (v.getClass().isArray()) {
					copyUnsignedArrayVals(v, vd);
				} else {
					if ("byte".equals(o) && ("int".equals(d) || "Integer".equals(d))) {
						Array.set(dest, i, byte_unsigned_to_int((byte) v));
					} else if ("byte".equals(o) && "long".equalsIgnoreCase(d)) {
						Array.set(dest, i, byte_unsigned_to_long((byte) v));
					} else if ("byte".equals(o) && "float".equalsIgnoreCase(d)) {
						Array.set(dest, i, byte_unsigned_to_float((byte) v));
					} else if ("byte".equals(o) && "double".equalsIgnoreCase(d)) {
						Array.set(dest, i, byte_unsigned_to_double((byte) v));
					} else if ("byte".equals(o) && "String".equals(d)) {
						Array.set(dest, i, byte_unsigned_to_String((byte) v));
					} else {
						Array.set(dest, i, v);
					}
				}
			} catch (Exception e) {
				throw new TFException("Problem converting array values ('" + o + "' to '" + d + "' with value '" + v
						+ "'): " + e.getMessage(), e);
			}
		}

	}

	/**
	 * Convert {@code Tensor<Double>} to double array.
	 * 
	 * @param tensor
	 *            The Tensor of Double values
	 * @return Primitive double array
	 */
	public static double[] doubleTensorToDoubleArray(Tensor<Double> tensor) {
		DoubleBuffer db = DoubleBuffer.allocate(tensor.numElements());
		tensor.writeTo(db);
		return db.array();
	}

	/**
	 * Convert {@code Tensor<Double>} to multidimensional double array.
	 * 
	 * @param tensor
	 *            The Tensor of Double values
	 * @return Multidimensional primitive double array as an Object
	 */
	public static Object doubleTensorToMultidimensionalDoubleArray(Tensor<Double> tensor) {
		int[] shape = lToI(tensor.shape());
		Object d = Array.newInstance(double.class, shape);
		tensor.copyTo(d);
		return d;
	}

	/**
	 * Convert 1d double array to float array.
	 * 
	 * @param dArray
	 *            Primitive double array
	 * @return Primitive float array
	 */
	public static float[] dToF(double[] dArray) {
		float[] fArray = new float[dArray.length];
		for (int i = 0; i < dArray.length; i++) {
			fArray[i] = (float) dArray[i];
		}
		return fArray;
	}

	/**
	 * Convert 1d double array to int array.
	 * 
	 * @param dArray
	 *            Primitive double array
	 * @return Primitive int array
	 */
	public static int[] dToI(double[] dArray) {
		int[] iArray = new int[dArray.length];
		for (int i = 0; i < dArray.length; i++) {
			iArray[i] = (int) dArray[i];
		}
		return iArray;
	}

	/**
	 * Convert 1d double array to long array.
	 * 
	 * @param dArray
	 *            Primitive double array
	 * @return Primitive long array
	 */
	public static long[] dToL(double[] dArray) {
		long[] lArray = new long[dArray.length];
		for (int i = 0; i < dArray.length; i++) {
			lArray[i] = (long) dArray[i];
		}
		return lArray;
	}

	/**
	 * Obtain the value of the first element of a (multidimensional) array.
	 * 
	 * @param obj
	 *            The multidimensional array
	 * @return The value of the first element of the multidimensional array
	 */
	public static Object firstElementValueOfMultidimArray(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj.getClass().isArray()) {
			return firstElementValueOfMultidimArray(Array.get(obj, 0));
		} else {
			return obj;
		}
	}

	/**
	 * Convert {@code Tensor<Float>} to float array.
	 * 
	 * @param tensor
	 *            The Tensor of Float values
	 * @return Primitive float array
	 */
	public static float[] floatTensorToFloatArray(Tensor<Float> tensor) {
		FloatBuffer fb = FloatBuffer.allocate(tensor.numElements());
		tensor.writeTo(fb);
		return fb.array();
	}

	/**
	 * Convert {@code Tensor<Float>} to multidimensional float array.
	 * 
	 * @param tensor
	 *            The Tensor of Float values
	 * @return Multidimensional primitive float array as an Object
	 */
	public static Object floatTensorToMultidimensionalFloatArray(Tensor<Float> tensor) {
		int[] shape = lToI(tensor.shape());
		Object f = Array.newInstance(float.class, shape);
		tensor.copyTo(f);
		return f;
	}

	/**
	 * Convert 1d float array to double array.
	 * 
	 * @param fArray
	 *            Primitive float array
	 * @return Primitive double array
	 */
	public static double[] fToD(float[] fArray) {
		double[] dArray = new double[fArray.length];
		for (int i = 0; i < fArray.length; i++) {
			dArray[i] = (double) fArray[i];
		}
		return dArray;
	}

	/**
	 * Convert 1d float array to int array.
	 * 
	 * @param fArray
	 *            Primitive float array
	 * @return Primitive int array
	 */
	public static int[] fToI(float[] fArray) {
		int[] iArray = new int[fArray.length];
		for (int i = 0; i < fArray.length; i++) {
			iArray[i] = (int) fArray[i];
		}
		return iArray;
	}

	/**
	 * Convert 1d float array to long array.
	 * 
	 * @param fArray
	 *            Primitive float array
	 * @return Primitive long array
	 */
	public static long[] fToL(float[] fArray) {
		long[] lArray = new long[fArray.length];
		for (int i = 0; i < fArray.length; i++) {
			lArray[i] = (long) fArray[i];
		}
		return lArray;
	}

	/**
	 * Obtain dimensions of an array.
	 * 
	 * @param array
	 *            Input array as an object
	 * @return Array dimensions as an array
	 */
	public static int[] getArrayDimensions(Object array) {
		List<Integer> dimList = getArrayDimensionsList(array);
		int[] dimensions = new int[dimList.size()];
		for (int i = 0; i < dimList.size(); i++) {
			dimensions[i] = dimList.get(i);
		}
		return dimensions;
	}

	/**
	 * Obtain a list of array dimensions based on an input array.
	 * 
	 * @param array
	 *            Input array as as object
	 * @return List of array dimensions
	 */
	public static List<Integer> getArrayDimensionsList(Object array) {
		if (array.getClass().isArray()) {
			List<Integer> dim = new ArrayList<Integer>();
			int length = Array.getLength(array);
			dim.add(length);
			Object v = Array.get(array, 0); // assume all have expected lengths
			List<Integer> dims = getArrayDimensionsList(v);
			if (dims != null) {
				dim.addAll(dims);
			}
			return dim;
		} else {
			return null;
		}

	}

	/**
	 * Convert {@code Tensor<Integer>} to int array.
	 * 
	 * @param tensor
	 *            The Tensor of Integer values
	 * @return Primitive int array
	 */
	public static int[] intTensorToIntArray(Tensor<Integer> tensor) {
		IntBuffer ib = IntBuffer.allocate(tensor.numElements());
		tensor.writeTo(ib);
		return ib.array();
	}

	/**
	 * Convert {@code Tensor<Integer>} to multidimensional int array.
	 * 
	 * @param tensor
	 *            The Tensor of Integer values
	 * @return Multidimensional primitive int array as an Object
	 */
	public static Object intTensorToMultidimensionalIntArray(Tensor<Integer> tensor) {
		int[] shape = lToI(tensor.shape());
		Object i = Array.newInstance(int.class, shape);
		tensor.copyTo(i);
		return i;
	}

	/**
	 * Convert 1d int array to double array.
	 * 
	 * @param iArray
	 *            Primitive int array
	 * @return Primitive double array
	 */
	public static double[] iToD(int[] iArray) {
		double[] dArray = new double[iArray.length];
		for (int i = 0; i < iArray.length; i++) {
			dArray[i] = (double) iArray[i];
		}
		return dArray;
	}

	/**
	 * Convert 1d int array to float array.
	 * 
	 * @param iArray
	 *            Primitive int array
	 * @return Primitive float array
	 */
	public static float[] iToF(int[] iArray) {
		float[] fArray = new float[iArray.length];
		for (int i = 0; i < iArray.length; i++) {
			fArray[i] = (float) iArray[i];
		}
		return fArray;
	}

	/**
	 * Convert 1d int array to long array.
	 * 
	 * @param iArray
	 *            Primitive int array
	 * @return Primitive long array
	 */
	public static long[] iToL(int[] iArray) {
		long[] lArray = new long[iArray.length];
		for (int i = 0; i < iArray.length; i++) {
			lArray[i] = (long) iArray[i];
		}
		return lArray;
	}

	/**
	 * Convert {@code Tensor<Long>} to long array.
	 * 
	 * @param tensor
	 *            The Tensor of Long values
	 * @return Primitive long array
	 */
	public static long[] longTensorToLongArray(Tensor<Long> tensor) {
		LongBuffer lb = LongBuffer.allocate(tensor.numElements());
		tensor.writeTo(lb);
		return lb.array();
	}

	/**
	 * Convert {@code Tensor<Long>} to multidimensional long array.
	 * 
	 * @param tensor
	 *            The Tensor of Long values
	 * @return Multidimensional primitive long array as an Object
	 */
	public static Object longTensorToMultidimensionalLongArray(Tensor<Long> tensor) {
		int[] shape = lToI(tensor.shape());
		Object l = Array.newInstance(long.class, shape);
		tensor.copyTo(l);
		return l;
	}

	/**
	 * Convert 1d long array to double array.
	 * 
	 * @param lArray
	 *            Primitive long array
	 * @return Primitive double array
	 */
	public static double[] lToD(long[] lArray) {
		double[] dArray = new double[lArray.length];
		for (int i = 0; i < lArray.length; i++) {
			dArray[i] = (double) lArray[i];
		}
		return dArray;
	}

	/**
	 * Convert 1d long array to float array.
	 * 
	 * @param lArray
	 *            Primitive long array
	 * @return Primitive float array
	 */
	public static float[] lToF(long[] lArray) {
		float[] fArray = new float[lArray.length];
		for (int i = 0; i < lArray.length; i++) {
			fArray[i] = (float) lArray[i];
		}
		return fArray;
	}

	/**
	 * Convert 1d long array to int array.
	 * 
	 * @param lArray
	 *            Primitive long array
	 * @return Primitive int array
	 */
	public static int[] lToI(long[] lArray) {
		int[] iArray = new int[lArray.length];
		for (int i = 0; i < lArray.length; i++) {
			iArray[i] = (int) lArray[i];
		}
		return iArray;
	}

	/**
	 * Obtain the index at which the maximum value occurs in an array.
	 * 
	 * @param d
	 *            The double array
	 * @return The index at which the maximum value occurs
	 */
	public static int maxIndex(double[] d) {
		int maxIndex = 0;
		double maxValue = Double.MIN_VALUE;
		for (int i = 0; i < d.length; i++) {
			if (d[i] > maxValue) {
				maxIndex = i;
				maxValue = d[i];
			}
		}
		return maxIndex;
	}

	/**
	 * Obtain the index at which the maximum value occurs in an array.
	 * 
	 * @param f
	 *            The float array
	 * @return The index at which the maximum value occurs
	 */
	public static int maxIndex(float[] f) {
		int maxIndex = 0;
		float maxValue = Float.MIN_VALUE;
		for (int i = 0; i < f.length; i++) {
			if (f[i] > maxValue) {
				maxIndex = i;
				maxValue = f[i];
			}
		}
		return maxIndex;
	}

	/**
	 * Obtain the indices at which the maximum values occur in the rows of a 2d array.
	 * 
	 * @param d
	 *            The double array
	 * @return The indices at which the maximum row values occur
	 */
	public static int[] maxIndices(double[][] d) {
		int[] maxIndices = new int[d.length];
		for (int i = 0; i < d.length; i++) {
			maxIndices[i] = maxIndex(d[i]);
		}
		return maxIndices;
	}

	/**
	 * Obtain the indices at which the maximum values occur in the rows of a 2d array.
	 * 
	 * @param f
	 *            The float array
	 * @return The indices at which the maximum row values occur
	 */
	public static int[] maxIndices(float[][] f) {
		int[] maxIndices = new int[f.length];
		for (int i = 0; i < f.length; i++) {
			maxIndices[i] = maxIndex(f[i]);
		}
		return maxIndices;
	}

	/**
	 * Convert a multidimensional (dim) byte array to a multidimensional (dim-1) String array. The multidimensional
	 * String array will have 1 less dimension than the multidimensional byte array since a 1D byte array is converted
	 * to a String.
	 * 
	 * @param b
	 *            The multidimensional byte array
	 * @return The multidimensional (dim-1) String array equivalent of the multidimensional (dim) byte array
	 */
	public static Object multidimBytesToMultidimStrings(Object b) {
		int[] bDim = getArrayDimensions(b);
		int[] sDim = Arrays.copyOf(bDim, bDim.length - 1);
		Object s = Array.newInstance(String.class, sDim);
		copyByteArrayToStringArrayVals(b, s, sDim.length);
		return s;
	}

	/**
	 * Convert a multidimensional (dim) String array to a multidimensional (dim+1) byte array. The multidimensional byte
	 * array will have 1 more dimension than the String array since a String is converted to a byte array.
	 * 
	 * @param s
	 *            The multidimensional String array
	 * @return The multidimensional (dim+1) byte array equivalent of the multidimensional (dim) String array
	 */
	public static Object multidimStringsToMultidimBytes(Object s) {
		int[] sDim = getArrayDimensions(s);
		int[] bDim = Arrays.copyOf(sDim, sDim.length + 1);
		Object b = Array.newInstance(byte.class, bDim);
		copyStringArrayToByteArrayVals(s, b);
		return b;
	}

	/**
	 * Convert {@code Tensor<String>} to multidimensional String array.
	 * 
	 * @param tensor
	 *            The Tensor of String values
	 * @return Multidimensional String array as an Object
	 */
	public static Object stringTensorToMultidimensionalStringArray(Tensor<String> tensor) {
		int[] sShape = lToI(tensor.shape());
		int[] bShape = Arrays.copyOf(sShape, sShape.length + 1);
		Object b = Array.newInstance(byte.class, bShape);
		tensor.copyTo(b);
		Object s = multidimBytesToMultidimStrings(b);
		return s;
	}

	/**
	 * Convert {@code Tensor<UInt8>} to byte array.
	 * 
	 * @param tensor
	 *            The Tensor of UInt8 values
	 * @return Primitive byte array
	 */
	public static byte[] uint8TensorToByteArray(Tensor<UInt8> tensor) {
		ByteBuffer bb = ByteBuffer.allocate(tensor.numElements());
		tensor.writeTo(bb);
		return bb.array();
	}

	/**
	 * Convert {@code Tensor<UInt8>} to multidimensional byte array.
	 * 
	 * @param tensor
	 *            The Tensor of UInt8 values
	 * @return Multidimensional primitive byte array as an Object
	 */
	public static Object uint8TensorToMultidimensionalByteArray(Tensor<UInt8> tensor) {
		int[] shape = lToI(tensor.shape());
		Object i = Array.newInstance(byte.class, shape);
		tensor.copyTo(i);
		return i;
	}

	/**
	 * Obtain an array of the first dimension of values of a multidimensional array.
	 * 
	 * @param obj
	 *            The multidimensional array
	 * @return Array of the first dimension of values of a multidimensional array
	 */
	public static Object firstDimensionValuesOfMultidimArray(Object obj) {
		int[] dim = getArrayDimensions(obj);
		Object first = firstElementValueOfMultidimArray(obj);
		Class<?> firstClass = first.getClass();
		Class<?> arrayType = null;
		// convert to primitive types if necessary
		if (firstClass.equals(Integer.class)) {
			arrayType = int.class;
		} else if (firstClass.equals(Long.class)) {
			arrayType = long.class;
		} else if (firstClass.equals(Float.class)) {
			arrayType = float.class;
		} else if (firstClass.equals(Double.class)) {
			arrayType = double.class;
		} else if (firstClass.equals(Boolean.class)) {
			arrayType = boolean.class;
		} else if (firstClass.equals(String.class)) {
			arrayType = String.class;
		} else if (firstClass.equals(Byte.class)) {
			arrayType = byte.class;
		} else {
			arrayType = firstClass;
		}
		Object a = Array.newInstance(arrayType, dim[0]);
		for (int i = 0; i < dim[0]; i++) {
			Object v = Array.get(obj, i);
			Array.set(a, i, traverseArrayToValue(v));
		}
		return a;
	}

	/**
	 * Traverse a multidimensional array at position 0 until a value is obtained.
	 * 
	 * @param obj
	 *            The multidimensional array
	 * @return The position 0 value of this multidimensional array.
	 */
	protected static Object traverseArrayToValue(Object obj) {
		if (obj.getClass().isArray()) {
			return traverseArrayToValue(Array.get(obj, 0));
		} else {
			return obj;
		}
	}

}
