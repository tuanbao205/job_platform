import { MigrationInterface, QueryRunner } from "typeorm";

export class CreateLocations1787128685000 implements MigrationInterface {
  name = "CreateLocations1787128685000";

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
      CREATE TABLE "provinces" (
        "code" varchar(20) NOT NULL,
        "name" varchar(255) NOT NULL,
        "full_name" varchar(255) NOT NULL,
        CONSTRAINT "pk_provinces_code" PRIMARY KEY ("code")
      )
    `);

    await queryRunner.query(`
      CREATE TABLE "wards" (
        "code" varchar(20) NOT NULL,
        "name" varchar(255) NOT NULL,
        "full_name" varchar(255) NOT NULL,
        "province_code" varchar(20) NOT NULL,
        CONSTRAINT "pk_wards_code" PRIMARY KEY ("code"),
        CONSTRAINT "fk_wards_province"
          FOREIGN KEY ("province_code")
          REFERENCES "provinces"("code")
          ON DELETE RESTRICT
          ON UPDATE CASCADE
      )
    `);

    await queryRunner.query(`
      CREATE INDEX "idx_wards_province_code"
      ON "wards" ("province_code")
    `);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`
      DROP INDEX "idx_wards_province_code"
    `);

    await queryRunner.query(`
      DROP TABLE "wards"
    `);

    await queryRunner.query(`
      DROP TABLE "provinces"
    `);
  }
}
