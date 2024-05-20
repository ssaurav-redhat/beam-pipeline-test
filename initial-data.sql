--
-- Database: `exampledb`
--

-- --------------------------------------------------------

--
-- Table structure for table `logs_table`
--

CREATE TABLE `logs_table` (
                              `uuid` varchar(255) NOT NULL,
                              `time` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `logs_table`
--

INSERT INTO `logs_table` (`uuid`, `time`) VALUES
                                              ('uuid-test-value-123', '2024-05-19 07:30:15'),
                                              ('uuid-test-123', '2024-05-19 07:30:47'),
                                              ('uuid-test-value-123', '2024-05-19 07:31:37'),
                                              ('uuid-test-123', '2024-05-19 07:31:55');
COMMIT;