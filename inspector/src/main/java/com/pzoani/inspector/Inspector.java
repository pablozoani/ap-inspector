package com.pzoani.inspector;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @param <T> The type of object to be validated.
 * @author Pablo Zoani
 */

public abstract class Inspector<T> implements Function<T, T> {

    private final InspectionRules inspectionRules = new InspectionRules();

    private final RuntimeException fallbackException;

    public Inspector(RuntimeException fallbackException) {
        this.fallbackException = fallbackException;
    }

    protected InspectionRules begin() {
        return inspectionRules;
    }

    @Override
    public T apply(T t) {
        return inspect(t);
    }

    public abstract T inspect(T t);

    protected class InspectionRules {

        private InspectionRules() {
        }

        public T end(T t) {
            return t;
        }

        public InspectionRules run(Runnable runnable) {
            runnable.run();
            return this;
        }

        // IS NOT NULL  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules isNotNull(RuntimeException exception,
            Object object
        ) {
            if (isNull(object)) throw exception;
            return this;
        }

        public InspectionRules isNotNull(Object object) {
            return isNotNull(fallbackException, object);
        }

        // ARE NOT NULL _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules areNotNull(RuntimeException exception,
            Object... objects
        ) {
            for (Object object : objects) {
                if (isNull(object)) throw exception;
            }
            return this;
        }

        public InspectionRules areNotNull(Object... objects) {
            return areNotNull(fallbackException, objects);
        }

        private boolean isNull(Object object) {
            return object == null;
        }

        // IS NOT BLANK _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules isNotBlank(RuntimeException exception,
            String string
        ) {
            if (isNull(string) || isBlank(string)) throw exception;
            return this;
        }

        public InspectionRules isNotBlank(String string) {
            return isNotBlank(fallbackException, string);
        }

        // ARE NOT BLANK    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules areNotBlank(RuntimeException exception,
            String... strings
        ) {
            for (String string : strings) {
                if (isNull(string) || isBlank(string)) throw exception;
            }
            return this;
        }

        public InspectionRules areNotBlank(String... strings) {
            return areNotBlank(fallbackException, strings);
        }

        private boolean isBlank(String string) {
            return string.trim().length() == 0;
        }

        // HAS LENGTH   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules hasLengthRange(int minLength, int maxLength,
            RuntimeException exception, CharSequence string
        ) {
            if (isNull(string) ||
                hasNotLengthRange(minLength, maxLength, string)
            ) {
                throw exception;
            }
            return this;
        }

        public InspectionRules hasLengthRange(int minLength, int maxLength,
            CharSequence string
        ) {
            return hasLengthRange(minLength, maxLength, fallbackException,
                string
            );
        }


        // HAVE LENGTH  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules haveLengthRange(int minLength, int maxLength,
            RuntimeException exception, CharSequence... strings
        ) {
            for (CharSequence string : strings) {
                if (isNull(string) ||
                    hasNotLengthRange(minLength, maxLength, string)
                ) {
                    throw exception;
                }
            }
            return this;
        }

        public InspectionRules haveLengthRange(int minLength, int maxLength,
            CharSequence... strings
        ) {
            return haveLengthRange(minLength, maxLength, fallbackException,
                strings);
        }

        private boolean hasNotLengthRange(int minLength, int maxLength,
            CharSequence string
        ) {
            int length = string.length();
            return (length < minLength || length > maxLength);
        }

        // IS POSITIVE  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        public InspectionRules isPositive(RuntimeException exception,
            BigDecimal number
        ) {
            if (number.signum() == -1) {
                throw exception;
            }
            return this;
        }

        public InspectionRules isPositive(RuntimeException exception,
            Double number
        ) {
            if (number < 0) {
                throw exception;
            }
            return this;
        }
    }
}
