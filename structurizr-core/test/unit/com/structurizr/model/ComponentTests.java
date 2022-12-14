package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    void getName_ReturnsTheGivenName_WhenANameIsGiven() {
        Component component = new Component();
        component.setName("Some name");
        assertEquals("Some name", component.getName());
    }

    @Test
    void getCanonicalName() {
        Component component = container.addComponent("Component", "Description");
        assertEquals("Component://System.Container.Component", component.getCanonicalName());
    }

    @Test
    void getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        Component component = container.addComponent("Name1/.Name2", "Description");
        assertEquals("Component://System.Container.Name1Name2", component.getCanonicalName());
    }

    @Test
    void getParent_ReturnsTheParentContainer() {
        Component component = container.addComponent("Component", "Description");
        assertEquals(container, component.getParent());
    }

    @Test
    void getContainer_ReturnsTheParentContainer() {
        Component component = container.addComponent("Name", "Description");
        assertEquals(container, component.getContainer());
    }

    @Test
    void removeTags_DoesNotRemoveRequiredTags() {
        Component component = new Component();
        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));

        component.removeTag(Tags.COMPONENT);
        component.removeTag(Tags.ELEMENT);

        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));
    }

    @Test
    void technologyProperty() {
        Component component = new Component();
        assertNull(component.getTechnology());

        component.setTechnology("Spring Bean");
        assertEquals("Spring Bean", component.getTechnology());
    }

    @Test
    void sizeProperty() {
        Component component = new Component();
        assertEquals(0, component.getSize());

        component.setSize(123456);
        assertEquals(123456, component.getSize());
    }

    @Test
    void setType_ThrowsAnExceptionWhenPassedNull() {
        Component component = new Component();
        try {
            component.setType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A fully qualified name must be provided.", iae.getMessage());
        }
    }

    @Test
    void setType_AddsAPrimaryCodeElement_WhenPassedAFullyQualifiedTypeName() {
        Component component = new Component();
        component.setType("com.structurizr.web.HomePageController");

        Set<CodeElement> codeElements = component.getCode();
        assertEquals(1, codeElements.size());
        CodeElement codeElement = codeElements.iterator().next();
        assertEquals("HomePageController", codeElement.getName());
        assertEquals("com.structurizr.web.HomePageController", codeElement.getType());
        assertEquals(CodeElementRole.Primary, codeElement.getRole());
    }

    @Test
    void setType_OverwritesThePrimaryCodeElement_WhenCalledMoreThanOnce() {
        Component component = new Component();
        component.setType("com.structurizr.web.HomePageController");
        component.setType("com.structurizr.web.SomeOtherController");

        Set<CodeElement> codeElements = component.getCode();
        assertEquals(1, codeElements.size());
        CodeElement codeElement = codeElements.iterator().next();
        assertEquals("SomeOtherController", codeElement.getName());
        assertEquals("com.structurizr.web.SomeOtherController", codeElement.getType());
        assertEquals(CodeElementRole.Primary, codeElement.getRole());

    }

    @Test
    void addSupportingType_ThrowsAnExceptionWhenPassedNull() {
        Component component = new Component();
        try {
            component.addSupportingType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A fully qualified name must be provided.", iae.getMessage());
        }
    }

    @Test
    void addSupportingType_AddsASupportingCodeElement_WhenPassedAFullyQualifiedTypeName() {
        Component component = new Component();
        component.addSupportingType("com.structurizr.web.HomePageViewModel");

        Set<CodeElement> codeElements = component.getCode();
        assertEquals(1, codeElements.size());
        CodeElement codeElement = codeElements.iterator().next();
        assertEquals("HomePageViewModel", codeElement.getName());
        assertEquals("com.structurizr.web.HomePageViewModel", codeElement.getType());
        assertEquals(CodeElementRole.Supporting, codeElement.getRole());
    }

    @Test
    void getType_ReturnsNull_WhenThereAreNoCodeElements() {
        Component component = new Component();
        assertNull(component.getType());
    }

    @Test
    void getType_ReturnsNull_WhenThereAreNoPrimaryCodeElements() {
        Component component = new Component();
        component.addSupportingType("com.structurizr.SomeType");
        assertNull(component.getType());
    }

    @Test
    void getType_ReturnsThePrimaryCodeElement_WhenThereIsAPrimaryCodeElement() {
        Component component = new Component();
        component.setType("com.structurizr.SomeType");
        CodeElement codeElement = component.getType();
        assertEquals(CodeElementRole.Primary, codeElement.getRole());
        assertEquals("com.structurizr.SomeType", codeElement.getType());
    }

}