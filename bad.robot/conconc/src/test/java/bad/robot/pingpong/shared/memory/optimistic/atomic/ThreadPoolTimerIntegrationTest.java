/*
 * Copyright (c) 2009-2011, bad robot (london) ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.pingpong.shared.memory.optimistic.atomic;

import bad.robot.pingpong.Introduce;
import bad.robot.pingpong.shared.memory.ThreadLocalMovableClock;
import bad.robot.pingpong.shared.memory.ThreadLocalStopWatch;
import bad.robot.pingpong.shared.memory.ThreadPoolTimer;
import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import static bad.robot.pingpong.shared.memory.Unguarded.unguarded;
import static com.google.code.tempusfugit.temporal.Duration.millis;
import static java.lang.Thread.currentThread;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ThreadPoolTimerIntegrationTest {

    private static final ThreadLocalMovableClock clock = new ThreadLocalMovableClock();
    private static final ThreadPoolTimer timer = new ThreadPoolTimer(unguarded(), new ThreadLocalStopWatch(clock), new AtomicLongCounter(), new AtomicLongCounter(), new AtomicMillisecondCounter());
    private static final Throwable NO_EXCEPTION = null;

    @Rule public RepeatingRule repeating = new RepeatingRule();
    @Rule public ConcurrentRule concurrent = new ConcurrentRule();

    @Concurrent (count = 50)
    @Repeating (repetition = 100)
    @Test
    public void executeTask() {
        Runnable task = newRunnable();
        timer.beforeExecute(currentThread(), task);
        clock.incrementBy(millis(400));
        timer.afterExecute(task, NO_EXCEPTION);
        Introduce.jitter();
    }

    @AfterClass
    public static void verifyCounters() {
        assertThat(timer.getNumberOfExecutions(), is(5000L));
        assertThat(timer.getMeanExecutionTime(), is(400L));
        assertThat(timer.getTerminated(), is(0L));
    }

    private static Runnable newRunnable() {
        return new Runnable() {
            @Override
            public void run() {
            }
        };
    }
}
