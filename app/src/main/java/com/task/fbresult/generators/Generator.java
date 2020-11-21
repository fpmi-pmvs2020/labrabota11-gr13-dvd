package com.task.fbresult.generators;

import java.util.List;

public interface Generator<T> {
    List<T> generate();
}
