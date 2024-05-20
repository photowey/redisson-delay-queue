/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.photowey.redisson.delay.queue.api.macher;

/**
 * {@code DelayedAntPathMatcher}
 *
 * @author photowey
 * @version 1.0.0
 * @since 2024/05/12
 */
public interface DelayedAntPathMatcher {

    String PATH_SEPARATOR = "/";

    String SINGLE_WORLD = "#";
    String MULTI_WORLD = "*";

    String SINGLE_PATH = "*";
    String MULTI_PATH = "**";

    /**
     * Determines if the given path matches the specified pattern.
     * <p>
     * This method first converts the given pattern and path to the Ant expression path format.
     * The conversion rules are as follows:
     * 1. If the pattern or path contains a period ('.') or a colon (':'), it will be escaped to a slash ('/').
     * 2. Supports wildcard characters:
     * - `#` represents a single word.
     * - `*` represents multiple words.
     * <p>
     * After conversion, the method uses AntPathMatcher to perform the matching.
     *
     * @param pattern The pattern to match, supporting wildcard characters and escaping.
     * @param path    The path to match, supporting wildcard characters and escaping.
     * @return true if the path matches the pattern; false otherwise.
     */
    boolean matches(String pattern, String path);

    default String toAntPattern(String expression) {
        return expression;
    }

    default String toAntPath(String expression) {
        return expression;
    }

}