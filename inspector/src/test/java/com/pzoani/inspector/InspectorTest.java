package com.pzoani.inspector;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InspectorTest {

    @Data
    @NoArgsConstructor
    private class Person {
        private String firstName;
        private String lastName;
        private Double height;

        public Person setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Person setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
    }

    Inspector<Person> inspector;

    @BeforeEach
    void setUp() {
        inspector = null;
    }

    @Test
    void example() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .areNotNull(person.getFirstName(), person.getLastName())
                    .end(person);
            }
        };

        p.setFirstName(null).setLastName(null);

        try {
            inspector.inspect(p);
        } catch (RuntimeException exc) {
            System.err.println("Person p is not valid");
        }
    }

    @Test
    void isNotNullTwoArgs() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .isNotNull(person.getFirstName())
                    .isNotNull(new IllegalArgumentException(),
                        person.getLastName()
                    ).end(person);
            }
        };

        p.setFirstName(null).setLastName("Doe");
        assertThrows(RuntimeException.class,
            () -> inspector.inspect(p)
        );

        p.setFirstName("John").setLastName(null);
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void isNotNullOneArg() {
        final Person p = new Person();
        inspector = new Inspector<>(new IllegalArgumentException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .isNotNull(person.getFirstName())
                    .end(person);
            }
        };

        p.setFirstName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );

        p.setFirstName("John");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void AreNotNullTwoArgs() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .areNotNull(new IllegalArgumentException(),
                        person.getFirstName(), person.getLastName()
                    ).end(person);
            }
        };

        p.setFirstName("John").setLastName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName(null).setLastName("Doe");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void areNotNullOneArg() {
        final Person p = new Person();
        inspector = new Inspector<>(new IllegalArgumentException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .areNotNull(person.getFirstName(), person.getLastName())
                    .end(person);
            }
        };

        p.setFirstName(null).setLastName("Doe");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void isNotBlankTwoArgs() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .isNotBlank(new IllegalArgumentException(),
                        person.getFirstName()
                    ).end(person);
            }
        };

        p.setFirstName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("John");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void isNotBlankOneArg() {
        final Person p = new Person();
        inspector = new Inspector<>(new IllegalArgumentException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .isNotBlank(person.getFirstName())
                    .end(person);
            }
        };

        p.setFirstName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("John");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void areNotBlankTwoArgs() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .areNotBlank(
                        new IllegalArgumentException(),
                        person.getFirstName(), person.getLastName()
                    ).end(person);
            }
        };

        p.setFirstName("").setLastName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName(null).setLastName("");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);


        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );

        p.setFirstName("John").setLastName("");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void areNotBlankOneArg() {
        final Person p = new Person();
        inspector = new Inspector<>(new IllegalArgumentException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .areNotBlank(person.getFirstName(), person.getLastName())
                    .end(person);
            }
        };

        p.setFirstName("");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void hasLengthRangeWithException() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .hasLengthRange(4, 10, new IllegalArgumentException(),
                        person.getFirstName()
                    ).end(person);
            }
        };

        p.setFirstName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );

        p.setFirstName("Joh");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("Internationalization");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("John");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void hasLengthRangeWithoutException() {
        final Person p = new Person();
        inspector = new Inspector<>(new IllegalArgumentException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .hasLengthRange(4, 10, person.getFirstName()
                    ).end(person);
            }
        };

        p.setFirstName(null);
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );

        p.setFirstName("Joh");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("Internationalization");
        assertThrows(IllegalArgumentException.class,
            () -> inspector.inspect(p)
        );
        p.setFirstName(null);

        p.setFirstName("John");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void haveLengthRangeWithException() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .haveLengthRange(2, 4, new IllegalArgumentException(),
                        person.getFirstName(), person.getLastName()
                    ).end(person);
            }
        };

        p.setFirstName("John").setLastName("D");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("J").setLastName("Doe");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("Johnn").setLastName("Doe");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doeee");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void haveLengthRangeWithoutException() {
        final Person p = new Person();
        inspector = new Inspector<>(new IllegalArgumentException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .haveLengthRange(2, 4, person.getFirstName(),
                        person.getLastName()
                    ).end(person);
            }
        };

        p.setFirstName("John").setLastName("D");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("J").setLastName("Doe");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("Johnn").setLastName("Doe");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doeee");
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });
        p.setFirstName(null).setLastName(null);

        p.setFirstName("John").setLastName("Doe");
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }

    @Test
    void isPositiveWithException() {
        final Person p = new Person();
        inspector = new Inspector<>(new RuntimeException()) {
            @Override
            public Person inspect(Person person) {
                return begin()
                    .isPositive(new IllegalArgumentException(),
                        person.getHeight()
                    ).end(person);
            }
        };

        p.setHeight(-1.82);
        assertThrows(IllegalArgumentException.class, () -> {
            inspector.inspect(p);
        });

        p.setHeight(0.0);
        Person inspected = inspector.inspect(p);
        assertEquals(p, inspected);
    }
}