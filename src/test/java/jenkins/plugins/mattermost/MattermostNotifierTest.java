package jenkins.plugins.mattermost;

import hudson.model.Descriptor;
import hudson.util.FormValidation;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MattermostNotifierTest extends TestCase {

	private MattermostNotifierStub.DescriptorImplStub descriptor;
	private MattermostServiceStub mattermostServiceStub;
	private boolean response;
	private FormValidation.Kind expectedResult;

	@Before
	@Override
	public void setUp() {
		descriptor = new MattermostNotifierStub.DescriptorImplStub();
	}

	public MattermostNotifierTest(MattermostServiceStub mattermostServiceStub, boolean response, FormValidation.Kind expectedResult) {
		this.mattermostServiceStub = mattermostServiceStub;
		this.response = response;
		this.expectedResult = expectedResult;
	}

	@Parameterized.Parameters
	public static Collection businessTypeKeys() {
		return Arrays.asList(new Object[][]{
			{new MattermostServiceStub(), true, FormValidation.Kind.OK},
				{new MattermostServiceStub(), false, FormValidation.Kind.ERROR},
				{null, false, FormValidation.Kind.ERROR}
		});
	}

	@Test
	public void testDoTestConnection() {
		if (mattermostServiceStub != null) {
			mattermostServiceStub.setResponse(response);
		}
		descriptor.setMattermostService(mattermostServiceStub);
		try {
			FormValidation result = descriptor.doTestConnection("host", "room", "", "buildServerUrl");
			assertEquals(result.kind, expectedResult);
		} catch (Descriptor.FormException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public static class MattermostServiceStub implements MattermostService {

		private boolean response;

		public boolean publish(String message) {
			return response;
		}

		public boolean publish(String message, String color) {
			return response;
		}

		public boolean publish(String message, String color, String text) {
			return response;
		}

		public void setResponse(boolean response) {
			this.response = response;
		}
	}
}
