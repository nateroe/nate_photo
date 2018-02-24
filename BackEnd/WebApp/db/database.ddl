
----------
-- DROP --
----------
DROP TABLE IF EXISTS UserRole;

DROP TABLE IF EXISTS Role;
DROP SEQUENCE IF EXISTS Role_id_seq;

DROP TABLE IF EXISTS Favorite;

DROP TABLE IF EXISTS Visit;

DROP TABLE IF EXISTS User;
DROP SEQUENCE IF EXISTS User_id_seq;

DROP TABLE IF EXISTS CommonName;

DROP TABLE IF EXISTS ExpeditionHighlight;

DROP TABLE IF EXISTS PhotoTag;

DROP TABLE IF EXISTS Photo;
DROP SEQUENCE IF EXISTS Photo_id_seq;

DROP TABLE IF EXISTS Expedition;
DROP SEQUENCE IF EXISTS Expedition_id_seq;

DROP TABLE IF EXISTS Place;
DROP SEQUENCE IF EXISTS Place_id_seq;

DROP TABLE IF EXISTS Tag;
DROP SEQUENCE IF EXISTS Tag_id_seq;

DROP TABLE IF EXISTS Taxon;
DROP SEQUENCE IF EXISTS Taxon_id_seq;

DROP TABLE IF EXISTS TaxonomicRank;
DROP SEQUENCE IF EXISTS TaxonomicRank_id_seq;

------------
-- CREATE --
------------
CREATE SEQUENCE public.role_id_seq;

CREATE TABLE public.Role (
                id BIGINT NOT NULL DEFAULT nextval('public.role_id_seq'),
                name VARCHAR NOT NULL,
                CONSTRAINT role_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.role_id_seq OWNED BY public.Role.id;

CREATE SEQUENCE public.taxonomicrank_id_seq;

CREATE TABLE public.TaxonomicRank (
                id BIGINT NOT NULL DEFAULT nextval('public.taxonomicrank_id_seq'),
                name VARCHAR NOT NULL,
                ordinal INTEGER NOT NULL,
                CONSTRAINT taxonomicrank_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.taxonomicrank_id_seq OWNED BY public.TaxonomicRank.id;

CREATE SEQUENCE public.taxon_id_seq;

CREATE TABLE public.Taxon (
                id BIGINT NOT NULL DEFAULT nextval('public.taxon_id_seq'),
                rankId BIGINT NOT NULL,
                name VARCHAR NOT NULL,
                tsn INTEGER NOT NULL,
                parentId BIGINT NOT NULL,
                CONSTRAINT taxon_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.taxon_id_seq OWNED BY public.Taxon.id;

CREATE SEQUENCE public.commonname_id_seq;

CREATE TABLE public.CommonName (
                id BIGINT NOT NULL DEFAULT nextval('public.commonname_id_seq'),
                taxonId BIGINT NOT NULL,
                commonName VARCHAR NOT NULL,
                CONSTRAINT commonname_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.commonname_id_seq OWNED BY public.CommonName.id;

CREATE INDEX commonname_idx
 ON public.CommonName
 ( taxonId );

CREATE SEQUENCE public.place_id_seq;

CREATE TABLE public.Place (
                id BIGINT NOT NULL DEFAULT nextval('public.place_id_seq'),
                name VARCHAR NOT NULL,
                county VARCHAR NOT NULL,
                state VARCHAR NOT NULL,
                country VARCHAR NOT NULL,
                CONSTRAINT place_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.place_id_seq OWNED BY public.Place.id;

CREATE TABLE public.Visitor (
                id BIGINT NOT NULL,
                email VARCHAR NOT NULL,
                password VARCHAR NOT NULL,
                salt VARCHAR NOT NULL,
                token VARCHAR NOT NULL,
                roleId BIGINT NOT NULL,
                CONSTRAINT visitor_pk PRIMARY KEY (id)
);


CREATE TABLE public.VisitorRole (
                visitorId BIGINT NOT NULL,
                roleId BIGINT NOT NULL,
                CONSTRAINT visitorrole_pk PRIMARY KEY (visitorId, roleId)
);


CREATE SEQUENCE public.tag_id_seq;

CREATE TABLE public.Tag (
                id BIGINT NOT NULL DEFAULT nextval('public.tag_id_seq'),
                value VARCHAR NOT NULL,
                CONSTRAINT tag_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.tag_id_seq OWNED BY public.Tag.id;

CREATE SEQUENCE public.expedition_id_seq;

CREATE TABLE public.Expedition (
                id BIGINT NOT NULL DEFAULT nextval('public.expedition_id_seq'),
                name VARCHAR NOT NULL,
                description VARCHAR NOT NULL,
                mapUrl VARCHAR NOT NULL,
                beginDate DATE NOT NULL,
                endDate DATE NOT NULL,
                placeId BIGINT NOT NULL,
                CONSTRAINT expedition_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.expedition_id_seq OWNED BY public.Expedition.id;

CREATE SEQUENCE public.photo_id_seq;

CREATE TABLE public.Photo (
                id BIGINT NOT NULL DEFAULT nextval('public.photo_id_seq'),
                title VARCHAR NOT NULL,
                description VARCHAR NOT NULL,
                rating INTEGER NOT NULL,
                width INTEGER NOT NULL,
                height INTEGER NOT NULL,
                path VARCHAR NOT NULL,
                date TIMESTAMP NOT NULL,
                mapUrl VARCHAR NOT NULL,
                camera VARCHAR NOT NULL,
                lens VARCHAR NOT NULL,
                aperture VARCHAR NOT NULL,
                shutterSpeed VARCHAR NOT NULL,
                iso VARCHAR NOT NULL,
                isFlashFired BOOLEAN NOT NULL,
                focusDistance REAL NOT NULL,
                copyright VARCHAR NOT NULL,
                expeditionId BIGINT NOT NULL,
                taxonId BIGINT NOT NULL,
                isPublished BOOLEAN NOT NULL,
                CONSTRAINT photo_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.photo_id_seq OWNED BY public.Photo.id;

CREATE TABLE public.ExpeditionHighlight (
                expeditionId BIGINT NOT NULL,
                photoId BIGINT NOT NULL,
                rank INTEGER NOT NULL,
                CONSTRAINT expeditionhighlight_pk PRIMARY KEY (expeditionId, photoId)
);


CREATE TABLE public.Favorite (
                visitorId BIGINT NOT NULL,
                photoId BIGINT NOT NULL,
                CONSTRAINT favorite_pk PRIMARY KEY (visitorId, photoId)
);


CREATE TABLE public.Visit (
                visitorId BIGINT NOT NULL,
                photoId BIGINT NOT NULL,
                CONSTRAINT visit_pk PRIMARY KEY (visitorId, photoId)
);


CREATE TABLE public.Photo_Tag (
                photoId BIGINT NOT NULL,
                tagId BIGINT NOT NULL,
                CONSTRAINT photo_tag_pk PRIMARY KEY (photoId, tagId)
);


ALTER TABLE public.VisitorRole ADD CONSTRAINT role_userrole_fk
FOREIGN KEY (roleId)
REFERENCES public.Role (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Taxon ADD CONSTRAINT taxonomicrank_taxon_fk
FOREIGN KEY (rankId)
REFERENCES public.TaxonomicRank (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Taxon ADD CONSTRAINT taxon_taxon_fk
FOREIGN KEY (parentId)
REFERENCES public.Taxon (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Photo ADD CONSTRAINT taxon_photo_fk
FOREIGN KEY (taxonId)
REFERENCES public.Taxon (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.CommonName ADD CONSTRAINT taxon_commonname_fk
FOREIGN KEY (taxonId)
REFERENCES public.Taxon (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Expedition ADD CONSTRAINT place_expedition_fk
FOREIGN KEY (placeId)
REFERENCES public.Place (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Visit ADD CONSTRAINT user_visit_fk
FOREIGN KEY (visitorId)
REFERENCES public.Visitor (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Favorite ADD CONSTRAINT user_favorite_fk
FOREIGN KEY (visitorId)
REFERENCES public.Visitor (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.VisitorRole ADD CONSTRAINT user_userrole_fk
FOREIGN KEY (visitorId)
REFERENCES public.Visitor (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Photo_Tag ADD CONSTRAINT tag_photo_tag_fk
FOREIGN KEY (tagId)
REFERENCES public.Tag (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Photo ADD CONSTRAINT trip_photo_fk
FOREIGN KEY (expeditionId)
REFERENCES public.Expedition (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ExpeditionHighlight ADD CONSTRAINT expedition_expeditionhighlight_fk
FOREIGN KEY (expeditionId)
REFERENCES public.Expedition (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Photo_Tag ADD CONSTRAINT photo_photo_tag_fk
FOREIGN KEY (photoId)
REFERENCES public.Photo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Visit ADD CONSTRAINT photo_visit_fk
FOREIGN KEY (photoId)
REFERENCES public.Photo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Favorite ADD CONSTRAINT photo_favorite_fk
FOREIGN KEY (photoId)
REFERENCES public.Photo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.ExpeditionHighlight ADD CONSTRAINT photo_expeditionhighlight_fk
FOREIGN KEY (photoId)
REFERENCES public.Photo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;