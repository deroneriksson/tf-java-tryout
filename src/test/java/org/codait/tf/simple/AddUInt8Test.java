package org.codait.tf.simple;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codait.tf.TFModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddUInt8Test {

	protected static Logger log = LogManager.getLogger(AddUInt8Test.class);

	public static final String ADD_UINT8_MODEL_DIR = "./simple/add_uint8";

	private TFModel model = null;

	@Before
	public void init() throws IOException {
		model = new TFModel(ADD_UINT8_MODEL_DIR).sig("serving_default");
		System.out.println("MODEL:" + model);
	}

	@After
	public void after() {
	}

	@Test
	public void inputByte1Byte2OutputInt() {
		int result = model.in("input1", (byte) 1).in("input2", (byte) 2).out("output").run().getInt("output");
		Assert.assertTrue(3 == result);
	}

	@Test
	public void inputByte127Byte1OutputInt() {
		int result = model.in("input1", (byte) 127).in("input2", (byte) 1).out("output").run().getInt("output");
		Assert.assertTrue(128 == result);
	}

	@Test
	public void inputByte254Byte1OutputInt() {
		int result = model.in("input1", (byte) 254).in("input2", (byte) 1).out("output").run().getInt("output");
		Assert.assertTrue(255 == result);
	}

	@Test
	public void inputByte255Byte1OutputInt() {
		int result = model.in("input1", (byte) 255).in("input2", (byte) 1).out("output").run().getInt("output");
		Assert.assertTrue(0 == result);
	}

	@Test
	public void inputByte1Byte2OutputByte() {
		byte result = model.in("input1", (byte) 1).in("input2", (byte) 2).out("output").run().getByte("output");
		Assert.assertTrue(3 == result);
	}

	@Test
	public void inputByte127Byte1OutputByte() {
		byte result = model.in("input1", (byte) 127).in("input2", (byte) 1).out("output").run().getByte("output");
		// adding 1 to Byte.MAX_VALUE should give -128 since java byte is signed
		Assert.assertTrue(-128 == result);
	}

	@Test
	public void inputInt1Int2OutputInt() {
		int result = model.in("input1", 1).in("input2", 2).out("output").run().getInt("output");
		Assert.assertTrue(3 == result);
	}

	@Test
	public void inputInt127Int1OutputInt() {
		int result = model.in("input1", 127).in("input2", 1).out("output").run().getInt("output");
		Assert.assertTrue(128 == result);
	}

	@Test
	public void inputInt254Int1OutputInt() {
		int result = model.in("input1", 254).in("input2", 1).out("output").run().getInt("output");
		Assert.assertTrue(255 == result);
	}

	@Test
	public void inputInt255Int1OutputInt() {
		int result = model.in("input1", 255).in("input2", 1).out("output").run().getInt("output");
		Assert.assertTrue(0 == result);
	}

	@Test
	public void inputLong1Long2OutputLong() {
		long result = model.in("input1", 1L).in("input2", 2L).out("output").run().getLong("output");
		Assert.assertTrue(3L == result);
	}

	@Test
	public void inputLong127Long1OutputLong() {
		long result = model.in("input1", 127L).in("input2", 1L).out("output").run().getLong("output");
		Assert.assertTrue(128L == result);
	}

	@Test
	public void inputLong254Long1OutputLong() {
		long result = model.in("input1", 254L).in("input2", 1L).out("output").run().getLong("output");
		Assert.assertTrue(255L == result);
	}

	@Test
	public void inputLong255Long1OutputLong() {
		long result = model.in("input1", 255L).in("input2", 1L).out("output").run().getLong("output");
		Assert.assertTrue(0L == result);
	}

	@Test
	public void inputFloat1Float2OutputFloat() {
		float result = model.in("input1", 1.0f).in("input2", 2.0f).out("output").run().getFloat("output");
		Assert.assertTrue(3.0f == result);
	}

	@Test
	public void inputFloat127Float1OutputFloat() {
		float result = model.in("input1", 127.0f).in("input2", 1.0f).out("output").run().getFloat("output");
		Assert.assertTrue(128.0f == result);
	}

	@Test
	public void inputFloat254Float1OutputFloat() {
		float result = model.in("input1", 254.0f).in("input2", 1.0f).out("output").run().getFloat("output");
		Assert.assertTrue(255.0f == result);
	}

	@Test
	public void inputFloat255Float1OutputFloat() {
		float result = model.in("input1", 255.0f).in("input2", 1.0f).out("output").run().getFloat("output");
		Assert.assertTrue(0.0f == result);
	}

	@Test
	public void inputDouble1Double2OutputDouble() {
		double result = model.in("input1", 1.0d).in("input2", 2.0d).out("output").run().getDouble("output");
		Assert.assertTrue(3.0d == result);
	}

	@Test
	public void inputDouble127Double1OutputDouble() {
		double result = model.in("input1", 127.0d).in("input2", 1.0d).out("output").run().getDouble("output");
		Assert.assertTrue(128.0d == result);
	}

	@Test
	public void inputDouble254Double1OutputDouble() {
		double result = model.in("input1", 254.0d).in("input2", 1.0d).out("output").run().getDouble("output");
		Assert.assertTrue(255.0d == result);
	}

	@Test
	public void inputDouble255Double1OutputDouble() {
		double result = model.in("input1", 255.0d).in("input2", 1.0d).out("output").run().getDouble("output");
		Assert.assertTrue(0.0d == result);
	}

	@Test
	public void inputString1String2OutputString() {
		String result = model.in("input1", "1").in("input2", "2").out("output").run().getString("output");
		Assert.assertTrue("3".equals(result));
	}

	@Test
	public void inputString127String1OutputString() {
		String result = model.in("input1", "127").in("input2", "1").out("output").run().getString("output");
		Assert.assertTrue("128".equals(result));
	}

	@Test
	public void inputString254String1OutputString() {
		String result = model.in("input1", "254").in("input2", "1").out("output").run().getString("output");
		Assert.assertTrue("255".equals(result));
	}

	public void inputString255String1OutputString() {
		String result = model.in("input1", "255").in("input2", "1").out("output").run().getString("output");
		Assert.assertTrue("0".equals(result));
	}

	@Test
	public void inputIntArraysOutputIntArray_1_2__3_4__4_6() {
		int[] result = model.in("input1", new int[] { 1, 2 }).in("input2", new int[] { 3, 4 }).out("output").run()
				.getIntArray("output");
		Assert.assertArrayEquals(new int[] { 4, 6 }, result);
	}

	@Test
	public void inputIntArraysOutputIntArray_127_254__1_1__128_255() {
		int[] result = model.in("input1", new int[] { 127, 254 }).in("input2", new int[] { 1, 1 }).out("output").run()
				.getIntArray("output");
		Assert.assertArrayEquals(new int[] { 128, 255 }, result);
	}

	@Test
	public void inputLongArraysOutputLongArray_1_2__3_4__4_6() {
		long[] result = model.in("input1", new long[] { 1L, 2L }).in("input2", new long[] { 3L, 4L }).out("output")
				.run().getLongArray("output");
		Assert.assertArrayEquals(new long[] { 4L, 6L }, result);
	}

	@Test
	public void inputLongArraysOutputLongArray_127_254__1_1__128_255() {
		long[] result = model.in("input1", new long[] { 127L, 254L }).in("input2", new long[] { 1L, 1L }).out("output")
				.run().getLongArray("output");
		Assert.assertArrayEquals(new long[] { 128L, 255L }, result);
	}

	@Test
	public void inputFloatArraysOutputFloatArray_1_2__3_4__4_6() {
		float[] result = model.in("input1", new float[] { 1.0f, 2.0f }).in("input2", new float[] { 3.0f, 4.0f })
				.out("output").run().getFloatArray("output");
		Assert.assertArrayEquals(new float[] { 4.0f, 6.0f }, result, 0.0f);
	}

	@Test
	public void inputFloatArraysOutputFloatArray_127_254__1_1__128_255() {
		float[] result = model.in("input1", new float[] { 127.0f, 254.0f }).in("input2", new float[] { 1.0f, 1.0f })
				.out("output").run().getFloatArray("output");
		Assert.assertArrayEquals(new float[] { 128.0f, 255.0f }, result, 0.0f);
	}

	@Test
	public void inputDoubleArraysOutputDoubleArray_1_2__3_4__4_6() {
		double[] result = model.in("input1", new double[] { 1.0d, 2.0d }).in("input2", new double[] { 3.0d, 4.0d })
				.out("output").run().getDoubleArray("output");
		Assert.assertArrayEquals(new double[] { 4.0d, 6.0d }, result, 0.0d);
	}

	@Test
	public void inputDoubleArraysOutputDoubleArray_127_254__1_1__128_255() {
		double[] result = model.in("input1", new double[] { 127.0d, 254.0d }).in("input2", new double[] { 1.0d, 1.0d })
				.out("output").run().getDoubleArray("output");
		Assert.assertArrayEquals(new double[] { 128.0d, 255.0d }, result, 0.0d);
	}

	@Test
	public void inputStringArraysOutputStringArray_1_2__3_4__4_6() {
		String[] result = model.in("input1", new String[] { "1", "2" }).in("input2", new String[] { "3", "4" })
				.out("output").run().getStringArray("output");
		Assert.assertArrayEquals(new String[] { "4", "6" }, result);
	}

	@Test
	public void inputStringArraysOutputStringArray_127_254__1_1__128_255() {
		String[] result = model.in("input1", new String[] { "127", "254" }).in("input2", new String[] { "1", "1" })
				.out("output").run().getStringArray("output");
		Assert.assertArrayEquals(new String[] { "128", "255" }, result);
	}
}
