package com.yoti.api.client.sandbox.profile.request;

import static org.bouncycastle.util.encoders.Base64.toBase64String;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import com.yoti.api.attributes.AttributeConstants.HumanProfileAttributes;
import com.yoti.api.client.DocumentDetails;
import com.yoti.api.client.sandbox.profile.request.attribute.SandboxAttribute;
import com.yoti.api.client.sandbox.profile.request.attribute.derivation.AgeVerification;
import com.yoti.api.client.spi.remote.DateValue;
import com.yoti.api.client.spi.remote.DocumentDetailsAttributeValue;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

public class YotiTokenRequestTest {

    private static final String SOME_ATTRIBUTE_NAME = "someAttributeName";
    private static final SandboxAttribute SOME_ATTRIBUTE = SandboxAttribute.builder()
            .name(SOME_ATTRIBUTE_NAME)
            .build();

    private static final String SOME_FAMILY_NAME = "someFamilyName";
    private static final String SOME_GIVEN_NAMES = "givenNames";
    private static final String SOME_FULL_NAME = "fullName";
    private static final String SOME_DOB = "1902-02-02";
    private static final String SOME_GENDER = "someGender";
    private static final String DOB_UNDER_18 = "2009-02-02";
    private static final String DOB_OVER_18 = "1978-02-02";
    private static final String SOME_POSTAL_ADDRESS = "somePostalAddress";
    private static final String SOME_NATIONALITY = "someNationality";
    private static final String SOME_PHONE_NUMBER = "somePhoneNumber";
    private static final String SOME_EMAIL_ADDRESS = "someEmailAddress";
    private static final String SOME_BASE_64_SELFIE = "someBase64Selfie";
    private static final String SOME_DOCUMENT_DETAILS = "someDocumentDetails";

    @Test
    public void shouldAddAnAttribute() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withAttribute(SOME_ATTRIBUTE)
                .build();

        assertEquals(1, yotiTokenRequest.getSandboxAttributes().size());
        assertEquals(SOME_ATTRIBUTE_NAME, yotiTokenRequest.getSandboxAttributes().get(0).getName());
    }

    @Test
    public void shouldCreateYotiTokenRequestWithImmutableList() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withAttribute(SOME_ATTRIBUTE)
                .build();

        try {
            yotiTokenRequest.getSandboxAttributes().add(SOME_ATTRIBUTE);
        } catch (UnsupportedOperationException e) {
            return;
        }

        fail("Expected an exception for modifying an immutable list");
    }

    @Test
    public void shouldAddAnAttributeOnlyOnce() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withAttribute(SOME_ATTRIBUTE)
                .withAttribute(SOME_ATTRIBUTE)
                .build();

        assertEquals(1, yotiTokenRequest.getSandboxAttributes().size());
        assertEquals(SOME_ATTRIBUTE_NAME, yotiTokenRequest.getSandboxAttributes().get(0).getName());
    }

    @Test
    public void shouldCreateRequestWithFamilyName() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withFamilyName(SOME_FAMILY_NAME)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.FAMILY_NAME, SOME_FAMILY_NAME)));
    }

    @Test
    public void shouldCreateRequestWithGivenNames() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withGivenNames(SOME_GIVEN_NAMES)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.GIVEN_NAMES, SOME_GIVEN_NAMES)));
    }

    @Test
    public void shouldCreateRequestWithFullName() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withFullName(SOME_FULL_NAME)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.FULL_NAME, SOME_FULL_NAME)));
    }

    @Test
    public void shouldCreateRequestWithGender() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withGender(SOME_GENDER)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.GENDER, SOME_GENDER)));
    }

    @Test
    public void shouldCreateRequestWithDateOfBirthFromString() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withDateOfBirth(SOME_DOB)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.DATE_OF_BIRTH, SOME_DOB)));
    }

    @Test
    public void shouldCreateRequestWithDateOfBirthFromDate() throws Exception {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withDateOfBirth(DateValue.parseFrom(SOME_DOB))
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.DATE_OF_BIRTH, SOME_DOB)));
    }

    @Test
    public void shouldCreateRequestWithAgeUnderVerification() {
        AgeVerification ageVerification = AgeVerification.builder()
                .withDateOfBirth(DOB_UNDER_18)
                .withAgeUnder(18)
                .build();
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withAgeVerification(ageVerification)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.derivedAttribute(HumanProfileAttributes.DATE_OF_BIRTH, DOB_UNDER_18, "age_under:18")));
    }

    @Test
    public void shouldCreateRequestWithAgeOverVerification() {
        AgeVerification ageVerification = AgeVerification.builder()
                .withDateOfBirth(DOB_OVER_18)
                .withAgeOver(18)
                .build();
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withAgeVerification(ageVerification)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.derivedAttribute(HumanProfileAttributes.DATE_OF_BIRTH, DOB_OVER_18, "age_over:18")));
    }

    @Test
    public void shouldCreateRequestWithPostalAddress() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withPostalAddress(SOME_POSTAL_ADDRESS)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.POSTAL_ADDRESS, SOME_POSTAL_ADDRESS)));
    }

    @Test
    public void shouldCreateRequestWithStructuredAddress() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withStructuredPostalAddress(SOME_POSTAL_ADDRESS)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.STRUCTURED_POSTAL_ADDRESS, SOME_POSTAL_ADDRESS)));
    }

    @Test
    public void shouldCreateRequestWithNationality() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withNationality(SOME_NATIONALITY)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.NATIONALITY, SOME_NATIONALITY)));
    }

    @Test
    public void shouldCreateRequestWithPhoneNumber() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withPhoneNumber(SOME_PHONE_NUMBER)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.PHONE_NUMBER, SOME_PHONE_NUMBER)));
    }

    @Test
    public void shouldCreateRequestWithEmailAddress() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withEmailAddress(SOME_EMAIL_ADDRESS)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.EMAIL_ADDRESS, SOME_EMAIL_ADDRESS)));
    }

    @Test
    public void shouldCreateRequestWithDocumentDetails() {
        DocumentDetails documentDetails = DocumentDetailsAttributeValue.builder()
                .withType("type")
                .withIssuingCountry("country")
                .withNumber("number")
                .build();
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withDocumentDetails(documentDetails)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.optionalAttribute(HumanProfileAttributes.DOCUMENT_DETAILS, "type country number")));
    }

    @Test
    public void shouldCreateRequestWithDocumentDetailsString() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withDocumentDetails(SOME_DOCUMENT_DETAILS)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.optionalAttribute(HumanProfileAttributes.DOCUMENT_DETAILS, SOME_DOCUMENT_DETAILS)));
    }

    @Test
    public void shouldCreateRequestWithSelfieBytes() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withSelfie(SOME_BASE_64_SELFIE.getBytes())
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.SELFIE, toBase64String(SOME_BASE_64_SELFIE.getBytes()))));
    }

    @Test
    public void shouldCreateRequestWithBase64Selfie() {
        YotiTokenRequest yotiTokenRequest = YotiTokenRequest.builder()
                .withBase64Selfie(SOME_BASE_64_SELFIE)
                .build();

        List<SandboxAttribute> result = yotiTokenRequest.getSandboxAttributes();

        assertThat(result, hasSize(1));
        assertThat(result, hasItem(AttributeMatcher.requiredAttribute(HumanProfileAttributes.SELFIE, SOME_BASE_64_SELFIE)));
    }

    private static class AttributeMatcher extends TypeSafeDiagnosingMatcher<SandboxAttribute> {

        private final String name;
        private final String value;
        private final String derivation;
        private final String optional;

        private AttributeMatcher(String name, String value, String derivation, String optional) {
            this.name = name;
            this.value = value;
            this.derivation = derivation;
            this.optional = optional;
        }

        @Override
        protected boolean matchesSafely(SandboxAttribute sandboxAttribute, Description description) {
            description.appendText(buildDescription(sandboxAttribute));
            return name.equals(sandboxAttribute.getName())
                    && value.equals(sandboxAttribute.getValue())
                    && derivation.equals(sandboxAttribute.getDerivation())
                    && optional.equals(sandboxAttribute.getOptional());
        }

        private static String buildDescription(SandboxAttribute sandboxAttribute) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ Name: " + sandboxAttribute.getName() + ", ");
            stringBuilder.append("Value: " + sandboxAttribute.getValue() + ", ");
            stringBuilder.append("Derivation: " + sandboxAttribute.getDerivation() + ", ");
            stringBuilder.append("Optional: " + sandboxAttribute.getOptional() + " }");
            return stringBuilder.toString();
        }

        @Override
        public void describeTo(Description description) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ Name: " + name + ", ");
            stringBuilder.append("Value: " + value + ", ");
            stringBuilder.append("Derivation: " + derivation + ", ");
            stringBuilder.append("Optional: " + optional + " }");
            description.appendText(stringBuilder.toString());
        }

        public static AttributeMatcher requiredAttribute(String name, String value) {
            return new AttributeMatcher(name, value, "", "false");
        }

        public static AttributeMatcher derivedAttribute(String name, String value, String derivation) {
            return new AttributeMatcher(name, value, derivation, "false");
        }

        public static AttributeMatcher optionalAttribute(String name, String value) {
            return new AttributeMatcher(name, value, "", "true");
        }

    }

}