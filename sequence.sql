-- Create the sequence with your custom starting number
CREATE SEQUENCE account_number_seq
    START WITH 1462000000
    INCREMENT BY 1
    MINVALUE 1462000000
    MAXVALUE 146200000000 -- Max value for BIGINT
    NOCACHE; -- Ensures no gaps but might have slight performance impact