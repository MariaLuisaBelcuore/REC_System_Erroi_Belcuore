package it.unisalento.rec.rec_usermanagement.RepositoryTest;

import it.unisalento.rec.rec_usermanagement.domain.Member;
import it.unisalento.rec.rec_usermanagement.repositories.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberRepositoryTest {
    @MockBean
    private MemberRepository memberRepository;

    @Test
    public void testFindMemberByEmail() {
        Member member = new Member();
        member.setEmail("member@example.com");
        member.setId("1");

        when(memberRepository.findMemberByEmail(anyString())).thenReturn(member);

        Member foundMember = memberRepository.findMemberByEmail(anyString());

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo("member@example.com");
        verify(memberRepository, times(1)).findMemberByEmail(anyString());
    }


    @Test
    public void testDeleteMemberById() {
        doNothing().when(memberRepository).deleteById(anyString());
        memberRepository.deleteById(anyString());
        verify(memberRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void testDeleteMemberByEmail() {
        doNothing().when(memberRepository).deleteByEmail(anyString());
        memberRepository.deleteByEmail(anyString());
        verify(memberRepository, times(1)).deleteByEmail(anyString());
    }

    @Test
    public void testFindMemberById() {
        Member member = new Member();
        member.setEmail("member@example.com");
        member.setId("1");

        when(memberRepository.findMemberById(anyString())).thenReturn(member);

        Member foundMember = memberRepository.findMemberById(anyString());

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getId()).isEqualTo("1");
        verify(memberRepository, times(1)).findMemberById(anyString());
    }


    @Test
    public void testExistsByEmail() {
        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = memberRepository.existsByEmail(anyString());

        assertThat(exists).isTrue();
        verify(memberRepository, times(1)).existsByEmail(anyString());
    }
}
