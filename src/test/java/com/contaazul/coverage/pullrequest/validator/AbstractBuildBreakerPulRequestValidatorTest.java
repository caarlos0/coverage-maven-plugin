package com.contaazul.coverage.pullrequest.validator;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.contaazul.coverage.cobertura.entity.Coverage;
import com.contaazul.coverage.github.GithubService;
import com.contaazul.coverage.pullrequest.UndercoveredException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class AbstractBuildBreakerPulRequestValidatorTest {
	@Mock
	private GithubService gh;
	@Mock
	private Coverage coverage;
	@Mock
	private CommitFile commitFile;

	private String src = "target/tmp/src/main/java";
	private int minCov = 100;

	private AbstractPullRequestValidator validator;

	@Before
	public void init() {
		initMocks( this );
	}

	@Test
	public void tesBlame() throws Exception {
		validator = new NonBuildBreakerPullRequestValidator( gh, coverage, src, minCov );
		mock();
		validator.validate();
		verify( gh, times( 1 ) ).createComment( anyString() );
	}

	@Test(expected = UndercoveredException.class)
	public void tesBlameAndBreak() throws Exception {
		validator = new BuildBreakerPullRequestValidator( gh, coverage, src, minCov );
		mock();
		validator.validate();
		verify( gh, times( 1 ) ).createComment( anyString() );
	}

	private void mock() throws IOException {
		final File patchFile = new File( "src/test/resources/2.patch" );
		String patch = Files.toString( patchFile, Charsets.UTF_8 );
		when( commitFile.getPatch() ).thenReturn( patch );
		when( commitFile.getFilename() ).thenReturn(
				"src/main/java/com/contaazul/coverage/pullrequest/PullRequestValidatorImpl.java" );
		when( gh.getPullRequestCommitFiles() ).thenReturn( Arrays.asList( commitFile ) );
	}


	@Test
	public void testNonBlame() throws Exception {
		validator = new NonBuildBreakerPullRequestValidator( gh, coverage, src, minCov );
		validator.validate();
		verify( gh, times( 0 ) ).createComment( anyString() );
	}
}