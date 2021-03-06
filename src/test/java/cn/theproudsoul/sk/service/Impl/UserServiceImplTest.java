package cn.theproudsoul.sk.service.Impl;

import cn.theproudsoul.sk.web.exception.UserAlreadyExistException;
import cn.theproudsoul.sk.persistence.model.UserModel;
import cn.theproudsoul.sk.persistence.repository.UserRepository;
import cn.theproudsoul.sk.web.vo.UserLoginVo;
import cn.theproudsoul.sk.web.vo.UserRegistrationVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserServiceImpl service;

    @Mock
    UserRepository repository;

    private UserModel model;

    private final static String username = UUID.randomUUID().toString();
    private final static String password = UUID.randomUUID().toString();
    private final static String email = UUID.randomUUID().toString()+ "@123.com";

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        service = new UserServiceImpl(repository);
        model = new UserModel();
        model.setUsername(username);
        model.setPassword(password);
        model.setEmail(email);
    }

    @Test
    void register() {
        UserRegistrationVo registrationVo = new UserRegistrationVo(email, username, password, password);

        when(repository.save(Mockito.eq(model))).thenReturn(1);
        when(repository.findByEmail(email)).thenReturn(null);

        // test success
        UserModel result = service.registerNewUserAccount(registrationVo);
        assertEquals(model, result);

        // test fail
        when(repository.findByEmail(email)).thenReturn(result);
        Exception exception = assertThrows(UserAlreadyExistException.class, () -> service.registerNewUserAccount(registrationVo));
        assertEquals("There is an account with that email address: " + email, exception.getMessage());

    }

    @Test
    void loginShouldSuccess() {
        UserLoginVo loginVo = new UserLoginVo(email, password);
        when(repository.findByEmail(email)).thenReturn(model);

        assertEquals(model, service.login(loginVo));
    }

    @Test
    void loginShouldFailI() {
        UserLoginVo loginVo = new UserLoginVo(email, password);
        when(repository.findByEmail(email)).thenReturn(null);

        assertNull(service.login(loginVo));
    }

    @Test
    void loginShouldFailII() {
        UserLoginVo loginVo = new UserLoginVo(email, password);
        model.setPassword(UUID.randomUUID().toString());
        when(repository.findByEmail(email)).thenReturn(model);

        assertNull(service.login(loginVo));
    }
}