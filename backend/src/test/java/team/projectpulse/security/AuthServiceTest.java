package team.projectpulse.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import team.projectpulse.instructor.Instructor;
import team.projectpulse.instructor.converter.InstructorToInstructorDtoConverter;
import team.projectpulse.student.converter.StudentToStudentDtoConverter;
import team.projectpulse.user.MyUserPrincipal;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    JwtProvider jwtProvider;
    @Mock
    StudentToStudentDtoConverter studentToStudentDtoConverter;
    @Mock
    InstructorToInstructorDtoConverter instructorToInstructorDtoConverter;

    @InjectMocks
    AuthService authService;

    /**
     * roles has no @NotEmpty, and InstructorService.updateInstructor saves whatever roles the
     * request sends, so an admin update without "roles" stores null. That user can still
     * authenticate, and login must not fail with a NullPointerException.
     */
    @Test
    @DisplayName("A user whose roles are null logs in without a NullPointerException")
    void createLoginInfo_userWithNullRoles_doesNotThrow() {
        Instructor instructor = new Instructor("n.roles@abc.edu", "No", "Roles", "n.roles@abc.edu", "123456", true, null);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(new MyUserPrincipal(instructor), null, List.of());

        Map<String, Object> loginInfo = this.authService.createLoginInfo(authentication);

        assertThat(loginInfo).isEmpty();
    }
}
