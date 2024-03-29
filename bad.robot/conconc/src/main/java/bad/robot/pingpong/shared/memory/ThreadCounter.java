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

package bad.robot.pingpong.shared.memory;

import com.google.code.tempusfugit.concurrency.annotations.ThreadSafe;

import static bad.robot.pingpong.shared.memory.Decrement.decrement;
import static bad.robot.pingpong.shared.memory.Increment.increment;
import static bad.robot.pingpong.shared.memory.Reset.resetOf;

@ThreadSafe
public class ThreadCounter implements ThreadObserver, ThreadCounterMBean {

    private final Counter activeThreads;
    private final Counter createdThreads;
    private final Guard guard;

    public ThreadCounter(Guard guard, Counter activeThreads, Counter createdThreads) {
        this.guard = guard;
        this.activeThreads = activeThreads;
        this.createdThreads = createdThreads;
    }

    @Override
    public void threadCreated() {
        guard.execute(increment(createdThreads));
    }

    @Override
    public void threadStarted() {
        guard.execute(increment(activeThreads));
    }

    @Override
    public void threadTerminated() {
        guard.execute(decrement(activeThreads));
    }

    @Override
    public Long getActiveThreads() {
        return activeThreads.get();
    }

    @Override
    public Long getCreatedThreads() {
        return createdThreads.get();
    }

    @Override
    public void reset() {
        if (guard.guarding())
            guard.execute(resetOf(activeThreads, createdThreads));
    }
}
