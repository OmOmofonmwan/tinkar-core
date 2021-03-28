package org.hl7.tinkar.entity;

import io.activej.bytebuf.ByteBuf;
import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.entity.internal.Get;
import org.hl7.tinkar.component.FieldDataType;
import org.hl7.tinkar.component.*;
import org.hl7.tinkar.terms.SemanticFacade;

import java.util.Arrays;

public class SemanticEntity
        extends Entity<SemanticEntityVersion>
        implements SemanticFacade, SemanticChronology<SemanticEntityVersion> {

    protected int referencedComponentNid;

    protected int typePatternNid;

    @Override
    protected int subclassFieldBytesSize() {
        return 4; // referenced component
    }

    @Override
    public FieldDataType dataType() {
        return FieldDataType.SEMANTIC_CHRONOLOGY;
    }

    @Override
    public ImmutableList<SemanticEntityVersion> versions() {
        return super.versions();
    }

    @Override
    protected void finishEntityWrite(ByteBuf byteBuf) {
        byteBuf.writeInt(referencedComponentNid);
        byteBuf.writeInt(typePatternNid);
    }

    @Override
    protected void finishEntityRead(ByteBuf readBuf, byte formatVersion) {
        this.referencedComponentNid = readBuf.readInt();
        this.typePatternNid = readBuf.readInt();
    }

    @Override
    protected void finishEntityRead(Chronology chronology) {
        if (chronology instanceof SemanticChronology semanticChronology) {
            referencedComponentNid = Get.entityService().nidForComponent(semanticChronology.referencedComponent());
            typePatternNid = Get.entityService().nidForComponent(semanticChronology.typePattern());
        }
    }

    @Override
    protected SemanticEntityVersion makeVersion(ByteBuf readBuf, byte formatVersion) {
        return SemanticEntityVersion.make(this, readBuf, formatVersion);
    }

    @Override
    protected SemanticEntityVersion makeVersion(Version version) {
        return SemanticEntityVersion.make(this, (SemanticVersion) version);
    }

    @Override
    public Entity referencedComponent() {
        return Get.entityService().getEntityFast(this.referencedComponentNid);
    }

    public int referencedComponentNid() {
        return this.referencedComponentNid;
    }

    public int typePatternNid() {
        return this.typePatternNid;
    }

    @Override
    public TypePattern typePattern() {
        return Get.entityService().getEntityFast(typePatternNid);
    }


    public static SemanticEntity make(ByteBuf readBuf, byte entityFormatVersion) {
        SemanticEntity semanticEntity = new SemanticEntity();
        semanticEntity.fill(readBuf, entityFormatVersion);
        return semanticEntity;
    }

    public static SemanticEntity make(SemanticChronology other) {
        SemanticEntity semanticEntity = new SemanticEntity();
        semanticEntity.fill(other);
        return semanticEntity;
    }

    @Override
    public String toString() {
        return "SemanticEntity{" +
                "type: " + PrimitiveData.text(typePatternNid) +
                " <" +
                nid +
                "> " + Arrays.toString(publicId().asUuidArray()) +
                ", rc: " + PrimitiveData.text(referencedComponentNid) +
                ", v: " + versions +
                '}';
    }
}
