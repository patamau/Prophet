package prophet;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import prophet.geom.Point2D;
import prophet.model.SimpleWorld;

class CoordinatesTest {

	@Test
	void latlongTest() {

		SimpleWorld w = new SimpleWorld();
		double x, y;
		
		x = y = 0d;
		Point2D p = new Point2D(x, y);
		Point2D polar = w.toPolar(p);
		Point2D cartesian = w.toCartesian(polar);
		assertEquals(cartesian.getX(), p.getX(), 0.1);
		assertEquals(cartesian.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);
		
		p.set(0, 20000);
		polar = w.toPolar(p);
		cartesian = w.toCartesian(polar);
		assertEquals(cartesian.getX(), p.getX(), 0.1);
		assertEquals(cartesian.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);
		
		p.set(10000, 0);
		polar = w.toPolar(p);
		cartesian = w.toCartesian(polar);
		assertEquals(cartesian.getX(), p.getX(), 0.1);
		assertEquals(cartesian.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);
		
		p.set(5000, 10000);
		polar = w.toPolar(p);
		cartesian = w.toCartesian(polar);
		assertEquals(cartesian.getX(), p.getX(), 0.1);
		assertEquals(cartesian.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);

		p.set(-1000, -5000);
		polar = w.toPolar(p);
		cartesian = w.toCartesian(polar);
		assertEquals(cartesian.getX(), p.getX(), 0.1);
		assertEquals(cartesian.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);
		
		p.set(0, 0);
		cartesian = w.toCartesian(p);
		polar = w.toPolar(cartesian);
		assertEquals(polar.getX(), p.getX(), 0.1);
		assertEquals(polar.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);

		p.set(45, 0);
		cartesian = w.toCartesian(p);
		polar = w.toPolar(cartesian);
		assertEquals(polar.getX(), p.getX(), 0.1);
		assertEquals(polar.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);
		

		p.set(90, -180);
		cartesian = w.toCartesian(p);
		polar = w.toPolar(cartesian);
		assertEquals(polar.getX(), p.getX(), 0.1);
		assertEquals(polar.getY(), p.getY(), 0.1);
		System.out.println("cartesian="+cartesian+" polar="+polar);
	}

}
