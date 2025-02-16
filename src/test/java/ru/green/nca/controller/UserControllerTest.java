package ru.green.nca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.green.nca.dto.UserDto;
import ru.green.nca.entity.User;
import ru.green.nca.enums.UserRole;
import ru.green.nca.security.JWTUtil;
import ru.green.nca.service.UserService;
import ru.green.nca.util.JsonConverter;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private JWTUtil jwtUtil;
    User USER = new User(1, "TheGreenUp", "12345678", "Даниил",
            "Гринь", "Сергеевич", null, null, UserRole.ADMIN);
    UserDto USER_DTO = new UserDto(1, "TheGreenUp", "Даниил",
            "Гринь", "Сергеевич", null, null, UserRole.ADMIN);

    @Test
    public void getAllUserTest() throws Exception {
        when(userService.getAllUsers(eq(0), eq(10))).thenReturn(List.of(USER_DTO));
        this.mockMvc.perform(get("/api/users?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(eq(3))).thenReturn(USER_DTO);
        this.mockMvc.perform(get("/api/users/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.createUser(USER_DTO)).thenReturn(USER_DTO);
        String UserJson = JsonConverter.asJsonString(USER);
        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UserJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
        verify(userService).deleteUser(1);
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userService.updateUser(eq(1), eq(USER_DTO))).thenReturn(USER_DTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.asJsonString(USER_DTO)))
                .andExpect(status().isOk());


        verify(userService).updateUser(eq(1), eq(USER_DTO));
    }
}
