package fr.berger.enhancedlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CoupleTest {
	
	Couple<String, Couple<Integer, Throwable>> couple;
	
	@BeforeEach
	public void setup() {
		couple = new Couple<>(
				"Hello world!",
				new Couple<>()
		);
		
		System.out.println("CoupleTest.setup> couple: " + couple.toString());
		
		Assertions.assertNotNull(couple);
		Assertions.assertNotNull(couple.getX());
		Assertions.assertNotNull(couple.getY());
		Assertions.assertNull(couple.getY().getX());
		Assertions.assertNull(couple.getY().getY());
		
		couple.getY().setX(3);
		couple.getY().setY(new IOException());
		
		System.out.println("CoupleTest.setup> couple: " + couple.toString());
		
		Assertions.assertNotNull(couple);
		Assertions.assertNotNull(couple.getX());
		Assertions.assertNotNull(couple.getY());
		Assertions.assertNotNull(couple.getY().getX());
		Assertions.assertNotNull(couple.getY().getY());
	}
	
	@Test
	public void test_class() {
		Assertions.assertTrue(couple.getX() instanceof String);
		Assertions.assertTrue(couple.getY().getX() instanceof Integer);
		Assertions.assertTrue(couple.getY().getY() instanceof IOException);
		Assertions.assertTrue(couple.getY().getY() instanceof Throwable);
		
		couple.getY().setY(new Error());
		System.out.println("CoupleTest.setup> test_class: " + couple.toString());
		
		Assertions.assertTrue(couple.getY().getY() instanceof Error);
		Assertions.assertTrue(couple.getY().getY() instanceof Throwable);
	}
	
	@Test
	public void test() {
		Assertions.assertEquals("Hello world!", couple.getX());
		Assertions.assertEquals(3, couple.getY().getX().intValue());
		Assertions.assertTrue(couple.getY().getY() instanceof IOException);
	}
}