package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Nested
@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class FilmDbStorageTest {

}
