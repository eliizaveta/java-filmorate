package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testAddFilm() throws Exception {
		mockMvc.perform(
				post("/films")
						.content("{\n" +
								"  \"name\": \"Name\",\n" +
								"  \"description\": \"Desc\",\n" +
								"  \"releaseDate\": \"2024-01-18\",\n" +
								"  \"duration\": 100\n" +
								"}")
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@Test
	void testAddUser() throws Exception {
		mockMvc.perform(
				post("/users")
						.content("{\n" +
								"  \"login\": \"lizand\",\n" +
								"  \"name\": \"Lisa\",\n" +
								"  \"email\": \"mail@gmail.com\",\n" +
								"  \"birthday\": \"2000-09-04\"\n" +
								"}")
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}
}
