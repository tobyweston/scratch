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

package bad.robot.pingpong.shared.memory.optimistic;

import akka.stm.Atomic;
import akka.stm.Ref;
import akka.stm.TransactionFactory;
import akka.stm.TransactionFactoryBuilder;
import bad.robot.pingpong.StopWatch;
import bad.robot.pingpong.shared.memory.ThreadPoolObserver;
import bad.robot.pingpong.shared.memory.ThreadPoolTimerMBean;

public class StmThreadPoolTimer implements ThreadPoolObserver, ThreadPoolTimerMBean {

    private final StopWatch timer;
    private final StmAtomicLongCounter tasks = new StmAtomicLongCounter();
    private final Ref<Long> terminated = new Ref<Long>(0L);
    private final TransactionalReferenceMillisecondCounter totalTime = new TransactionalReferenceMillisecondCounter();
//    private final StmMillisecondCounter totalTime = new StmMillisecondCounter();

    public StmThreadPoolTimer(StopWatch timer) {
        this.timer = timer;
    }

    @Override
    public void beforeExecute(Thread thread, Runnable task) {
        assert (Thread.currentThread().equals(thread));
        timer.start();
        tasks.increment();
    }

    @Override
    public void afterExecute(Runnable task, Throwable throwable) {
        timer.stop();
//        new StmGuard().execute(new Callable<Void, RuntimeException>() {
//            @Override
//            public Void call() throws RuntimeException {
                totalTime.add(timer.elapsedTime());
//                return null;
//            }
//        });
    }

    @Override
    public void terminated() {
        new Atomic<Void>() {
            @Override
            public Void atomically() {
                terminated.set(terminated.get() + 1L);
                return null;
            }
        }.execute();
    }

    @Override
    public Long getNumberOfExecutions() {
        return tasks.get();
    }

    @Override
    public Long getTotalTime() {
        return totalTime.get();
    }

    @Override
    public Long getMeanExecutionTime() {
        TransactionFactory factory = new TransactionFactoryBuilder().setReadonly(true).build();
        return new Atomic<Long>(factory) {
            @Override
            public Long atomically() {
                return totalTime.get() / tasks.get();
            }
        }.execute();
    }

    @Override
    public Long getTerminated() {
        return terminated.get();
    }

    @Override
    public void reset() {
    }


}