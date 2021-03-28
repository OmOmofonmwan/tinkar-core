package org.hl7.tinkar.coordinate.stamp;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.primitive.ImmutableIntSet;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.component.Concept;

/**
 * Consider simplifying by merging with StampFilter.
 */
public interface StampFilterTemplate {

    /**
     * Determine what states should be included in results based on this
     * stamp coordinate. If current—but inactive—versions are desired,
     * the allowed states must include {@code Status.INACTIVE}
     *
     * @return the set of allowed states for results based on this stamp coordinate.
     */
    StateSet allowedStates();
    /**
     * An empty array is a wild-card, and should match all modules. If there are
     * one or more module nids specified, only those modules will be included
     * in the results.
     * @return an unmodifiable set of module nids to include in results based on this
     * stamp coordinate.
     */
    ImmutableIntSet moduleNids();

    /**
     * An empty array indicates that no modules should be excluded. If there are
     * one or more module nids specified, only those modules will be excluded
     * from the results.
     * @return an unmodifiable set of module nids to exclude in results based on this
     * stamp filter.
     */
    ImmutableIntSet excludedModuleNids();

    /**
     * An empty array indicates that no modules should be excluded. If there are
     * one or more module nids specified, only those modules will be excluded
     * from the results.
     * @return an unmodifiable set of modules to exclude in results based on this
     * stamp filter.
     */
    default ImmutableSet<Concept> excludedModules() {
        return excludedModuleNids().collect(nid -> Entity.getFast(nid));
    }

    /**
     * An empty list is a wild-card, and should match all modules. If there are
     * one or more modules specified, only those modules will be included
     * in the results.
     * @return an unmodifiable set of modules to include in results based on this
     * stamp coordinate.
     */
    default ImmutableSet<Concept> moduleSpecifications() {
        return moduleNids().collect(nid -> Entity.getFast(nid));

    }

    /**
     * Gets the module preference list for versions. Used to adjudicate which component to
     * return when more than one version is available. For example, if two modules
     * have versions the same component, which one do you prefer to return?
     * @return an unmodifiable module preference list for versions.
     */

    ImmutableIntList modulePriorityOrder();

    /**
     * Gets the module preference list for versions. Used to adjudicate which component to
     * return when more than one version is available. For example, if two modules
     * have versions the same component, which one do you prefer to return?
     * @return an unmodifiable module preference list for versions.
     */

    default ImmutableList<Concept> modulePriorityOrderSpecifications() {
        return modulePriorityOrder().collect(nid -> Entity.getFast(nid));
    }

    /**
     * @return multi-line string output suitable for presentation to user, as opposed to use in debugging.
     */

    default String toUserString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("   allowed states: ");
        builder.append(this.allowedStates().toUserString());

        builder.append("\n   modules: ");

        if (this.moduleNids().isEmpty()) {
            builder.append("all ");
        } else {
            builder.append(PrimitiveData.textList(this.moduleNids().toArray()))
                    .append(" ");
        }

        builder.append("\n   module priorities: ");
        if (this.modulePriorityOrder().isEmpty()) {
            builder.append("none ");
        } else {
            builder.append(PrimitiveData.textList(this.modulePriorityOrder().toArray()))
                    .append(" ");
        }

        return builder.toString();
    }

    StampFilterTemplateImmutable toStampFilterTemplateImmutable();

}
