package de.geolykt.starloader.micromixin.test.j8;

import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import de.geolykt.starloader.micromixin.test.j8.targets.InjectionHeadTest;
import de.geolykt.starloader.micromixin.test.j8.targets.MultiInjectTest;

public class TestHarness {

    public static TestReport runAllTests() {
        TestReport report = new TestReport();
        runInjectionHeadTests(report);
        runMultiInjectionTests(report);
        return report;
    }

    public static void runMultiInjectionTests(TestReport report) {
        TestSet set = new TestSet();
        set.addUnit("MultiInjectTest.injectionPointA0", MultiInjectTest::injectionPointA0);
        set.addUnit("MultiInjectTest.injectionPointA1", MultiInjectTest::injectionPointA1);
        set.addUnit("MultiInjectTest.injectionPointA2", MultiInjectTest::injectionPointA2);
        set.addUnit("MultiInjectTest.injectionPointA3", MultiInjectTest::injectionPointA3);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointACount", MultiInjectTest::getInjectionPointACount, 4);
        set.addUnit("MultiInjectTest.injectionPointB0", MultiInjectTest::injectionPointB0);
        set.addUnit("MultiInjectTest.injectionPointB1", MultiInjectTest::injectionPointB1);
        set.addUnit("MultiInjectTest.injectionPointB2", MultiInjectTest::injectionPointB2);
        set.addUnit("MultiInjectTest.injectionPointB3", MultiInjectTest::injectionPointB3);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointBCount", MultiInjectTest::getInjectionPointBCount, 4);
        set.addUnit("MultiInjectTest.injectionPointC0", MultiInjectTest::injectionPointC0);
        set.addUnit("MultiInjectTest.injectionPointC1", MultiInjectTest::injectionPointC1);
        set.addUnit("MultiInjectTest.injectionPointC2", MultiInjectTest::injectionPointC2);
        set.addUnit("MultiInjectTest.injectionPointC3", MultiInjectTest::injectionPointC3);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointCCount", MultiInjectTest::getInjectionPointCCount, 0);
        set.addUnit("MultiInjectTest.injectionPointD0", MultiInjectTest::injectionPointD0);
        set.addUnit("MultiInjectTest.injectionPointD1", MultiInjectTest::injectionPointD1);
        set.addUnit("MultiInjectTest.injectionPointD2", MultiInjectTest::injectionPointD2);
        set.addUnit("MultiInjectTest.injectionPointD3", MultiInjectTest::injectionPointD3);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointDCount", MultiInjectTest::getInjectionPointDCount, 4);
        set.addUnit("MultiInjectTest.injectionPointE0", MultiInjectTest::injectionPointE0);
        set.addUnit("MultiInjectTest.injectionPointE1", MultiInjectTest::injectionPointE1);
        set.addUnit("MultiInjectTest.injectionPointE2", MultiInjectTest::injectionPointE2);
        set.addUnit("MultiInjectTest.injectionPointE3", MultiInjectTest::injectionPointE3);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointECount", MultiInjectTest::getInjectionPointECount, 1);
        set.addUnit("MultiInjectTest.injectionPointF0", MultiInjectTest::injectionPointF0);
        set.addUnit("MultiInjectTest.injectionPointF1", MultiInjectTest::injectionPointF1);
        set.addUnit("MultiInjectTest.injectionPointF2", MultiInjectTest::injectionPointF2);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointFCount", MultiInjectTest::getInjectionPointFCount, 2);
        set.addUnit("MultiInjectTest.injectionPointG0", MultiInjectTest::injectionPointG0);
        set.addUnit("MultiInjectTest.injectionPointG1", MultiInjectTest::injectionPointG1);
        set.addUnit("MultiInjectTest.injectionPointG2", MultiInjectTest::injectionPointG2);
        set.addUnitAssertEquals("MultiInjectTest.getInjectionPointGCount", MultiInjectTest::getInjectionPointGCount, 1);
        LoggerFactory.getLogger(TestHarness.class).info("MultiInjectTest:");
        set.executeAll(report, LoggerFactory.getLogger(TestHarness.class));
    }

    public static void runInjectionHeadTests(TestReport report) {
        TestSet set = new TestSet();
        set.addUnit("InjectionHeadTest.expectNoThrowV", InjectionHeadTest::expectNoThrowV);
        set.addUnit("InjectionHeadTest.expectNoThrowB", InjectionHeadTest::expectNoThrowB);
        set.addUnit("InjectionHeadTest.expectNoThrowC", InjectionHeadTest::expectNoThrowC);
        set.addUnit("InjectionHeadTest.expectNoThrowZ", InjectionHeadTest::expectNoThrowZ);
        set.addUnit("InjectionHeadTest.expectNoThrowS", InjectionHeadTest::expectNoThrowS);
        set.addUnit("InjectionHeadTest.expectNoThrowI", InjectionHeadTest::expectNoThrowI);
        set.addUnit("InjectionHeadTest.expectNoThrowJ", InjectionHeadTest::expectNoThrowJ);
        set.addUnit("InjectionHeadTest.expectNoThrowF", InjectionHeadTest::expectNoThrowF);
        set.addUnit("InjectionHeadTest.expectNoThrowD", InjectionHeadTest::expectNoThrowD);
        set.addUnit("InjectionHeadTest.expectNoThrowL", InjectionHeadTest::expectNoThrowL);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowV", InjectionHeadTest::expectThrowV);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowB", InjectionHeadTest::expectThrowB);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowC", InjectionHeadTest::expectThrowC);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowZ", InjectionHeadTest::expectThrowZ);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowS", InjectionHeadTest::expectThrowS);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowI", InjectionHeadTest::expectThrowI);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowJ", InjectionHeadTest::expectThrowJ);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowF", InjectionHeadTest::expectThrowF);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowD", InjectionHeadTest::expectThrowD);
        set.addUnitExpectThrow("InjectionHeadTest.expectThrowL", InjectionHeadTest::expectThrowL);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationB", InjectionHeadTest::expectImplicitlyInvalidCancellationB, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationC", InjectionHeadTest::expectImplicitlyInvalidCancellationC, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationD", InjectionHeadTest::expectImplicitlyInvalidCancellationD, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationF", InjectionHeadTest::expectImplicitlyInvalidCancellationF, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationI", InjectionHeadTest::expectImplicitlyInvalidCancellationI, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationJ", InjectionHeadTest::expectImplicitlyInvalidCancellationJ, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationL", InjectionHeadTest::expectImplicitlyInvalidCancellationL, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationS", InjectionHeadTest::expectImplicitlyInvalidCancellationS, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationV", InjectionHeadTest::expectImplicitlyInvalidCancellationV, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectImplicitlyInvalidCancellationZ", InjectionHeadTest::expectImplicitlyInvalidCancellationZ, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationB", InjectionHeadTest::expectInvalidCancellationB, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationC", InjectionHeadTest::expectInvalidCancellationC, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationD", InjectionHeadTest::expectInvalidCancellationD, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationF", InjectionHeadTest::expectInvalidCancellationF, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationI", InjectionHeadTest::expectInvalidCancellationI, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationJ", InjectionHeadTest::expectInvalidCancellationJ, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationL", InjectionHeadTest::expectInvalidCancellationL, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationS", InjectionHeadTest::expectInvalidCancellationS, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationV", InjectionHeadTest::expectInvalidCancellationV, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectInvalidCancellationZ", InjectionHeadTest::expectInvalidCancellationZ, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultB", InjectionHeadTest::expectReturnNondefaultB, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultC", InjectionHeadTest::expectReturnNondefaultC, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultD", InjectionHeadTest::expectReturnNondefaultD, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultF", InjectionHeadTest::expectReturnNondefaultF, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultI", InjectionHeadTest::expectReturnNondefaultI, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultJ", InjectionHeadTest::expectReturnNondefaultJ, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultL", InjectionHeadTest::expectReturnNondefaultL, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultS", InjectionHeadTest::expectReturnNondefaultF, CancellationException.class);
        set.addUnitExpectThrow("InjectionHeadTest.expectReturnNondefaultZ", InjectionHeadTest::expectReturnNondefaultZ, CancellationException.class);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultB", InjectionHeadTest::expectCancellableReturnNondefaultB, (char) 0);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultC", InjectionHeadTest::expectCancellableReturnNondefaultC, (char) 0);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultD", InjectionHeadTest::expectCancellableReturnNondefaultD, 0D);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultF", InjectionHeadTest::expectCancellableReturnNondefaultF, 0F);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultI", InjectionHeadTest::expectCancellableReturnNondefaultI, 0);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultJ", InjectionHeadTest::expectCancellableReturnNondefaultJ, 0L);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultL", InjectionHeadTest::expectCancellableReturnNondefaultL, null);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultS", InjectionHeadTest::expectCancellableReturnNondefaultF, 0F);
        set.addUnitAssertNotEquals("InjectionHeadTest.expectCancellableReturnNondefaultZ", InjectionHeadTest::expectCancellableReturnNondefaultZ, false);
        LoggerFactory.getLogger(TestHarness.class).info("InjectionHeadTest:");
        set.executeAll(report, LoggerFactory.getLogger(TestHarness.class));
    }
}
