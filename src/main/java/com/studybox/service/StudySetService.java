package com.studybox.service;

import com.studybox.dao.StudySetDao;
import com.studybox.exception.NotFoundException;
import com.studybox.exception.ValidationException;
import com.studybox.model.StudySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class StudySetService {

    private static final Set<String> ALLOWED_STATUSES = Set.of("ACTIVE", "ARCHIVED");
    private final StudySetDao studySetDao;

    public StudySetService() {
        this(new StudySetDao());
    }

    public StudySetService(StudySetDao studySetDao) {
        this.studySetDao = studySetDao;
    }

    public List<StudySet> getOwnedStudySets(long userId) {
        return studySetDao.findAllByUser(userId);
    }

    public List<StudySet> getRecentOwnedStudySets(long userId, int limit) {
        return studySetDao.findRecentByUser(userId, limit);
    }

    public long countOwnedSets(long userId) {
        return studySetDao.countByUser(userId);
    }

    public long countActiveOwnedSets(long userId) {
        return studySetDao.countByUserAndStatus(userId, "ACTIVE");
    }

    public long countArchivedOwnedSets(long userId) {
        return studySetDao.countByUserAndStatus(userId, "ARCHIVED");
    }

    public StudySet getOwnedStudySet(long userId, long studySetId) {
        return studySetDao.findById(studySetId, userId)
                .orElseThrow(() -> new NotFoundException("Study Set không tồn tại hoặc bạn không có quyền truy cập."));
    }

    public StudySet createStudySet(long userId, StudySet form) {
        StudySet normalized = normalize(form);
        validate(userId, normalized, null);
        normalized.setUserId(userId);
        studySetDao.create(normalized);
        return normalized;
    }

    public StudySet updateStudySet(long userId, long studySetId, StudySet form) {
        StudySet current = getOwnedStudySet(userId, studySetId);
        StudySet normalized = normalize(form);
        normalized.setId(current.getId());
        normalized.setUserId(userId);
        validate(userId, normalized, studySetId);
        if (!studySetDao.update(normalized)) {
            throw new NotFoundException("Không thể cập nhật Study Set.");
        }
        return normalized;
    }

    public void deleteStudySet(long userId, long studySetId) {
        if (!studySetDao.delete(studySetId, userId)) {
            throw new NotFoundException("Study Set không tồn tại hoặc bạn không có quyền truy cập.");
        }
    }

    private void validate(long userId, StudySet studySet, Long excludeId) {
        List<String> errors = new ArrayList<>();

        if (userId <= 0) {
            errors.add("Người dùng không hợp lệ.");
        }
        if (studySet.getName().isEmpty()) {
            errors.add("Vui lòng nhập tên bộ học tập.");
        }
        if (studySet.getName().length() > 150) {
            errors.add("Tên bộ học tập không được vượt quá 150 ký tự.");
        }
        if (studySet.getSubject().isEmpty()) {
            errors.add("Vui lòng nhập chủ đề.");
        }
        if (studySet.getSubject().length() > 100) {
            errors.add("Chủ đề không được vượt quá 100 ký tự.");
        }
        if (studySet.getDescription() != null && studySet.getDescription().length() > 5000) {
            errors.add("Mô tả không được vượt quá 5000 ký tự.");
        }
        if (!ALLOWED_STATUSES.contains(studySet.getStatus())) {
            errors.add("Trạng thái không hợp lệ.");
        }
        if (!studySet.getName().isEmpty() && studySetDao.existsByUserAndName(userId, studySet.getName(), excludeId)) {
            errors.add("Tên bộ học tập đã tồn tại.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private StudySet normalize(StudySet source) {
        StudySet studySet = new StudySet();
        studySet.setName(trim(source.getName()));
        studySet.setDescription(trimToNull(source.getDescription()));
        studySet.setSubject(trim(source.getSubject()));
        studySet.setStatus(normalizeStatus(source.getStatus()));
        return studySet;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private String trimToNull(String value) {
        String trimmed = trim(value);
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeStatus(String value) {
        String trimmed = trim(value);
        if (trimmed.isEmpty()) {
            return "ACTIVE";
        }
        return trimmed.toUpperCase(Locale.ROOT);
    }
}
