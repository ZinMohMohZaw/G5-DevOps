
# World Population Reporting System

[![Master Build Status](https://img.shields.io/github/actions/workflow/status/ZinMohMohZaw/G5-DevOps/build.yml?branch=master&label=master)](https://github.com/ZinMohMohZaw/G5-DevOps/actions/workflows/build.yml)
[![Develop Build Status](https://img.shields.io/github/actions/workflow/status/ZinMohMohZaw/G5-DevOps/build.yml?branch=develop&label=develop)](https://github.com/ZinMohMohZaw/G5-DevOps/actions/workflows/build.yml)
[![Code Coverage](https://img.shields.io/badge/coverage-0%25-red.svg)](https://github.com/ZinMohMohZaw/G5-DevOps)
[![Release](https://img.shields.io/github/v/release/ZinMohMohZaw/G5-DevOps?include_prereleases)](https://github.com/ZinMohMohZaw/G5-DevOps/releases)
[![License](https://img.shields.io/github/license/ZinMohMohZaw/G5-DevOps)](https://github.com/ZinMohMohZaw/G5-DevOps/blob/master/LICENSE)
# Team Working Agreement & Governance

This document outlines our team structure, ethics, operational guidelines, development standards, and GitFlow workflow to ensure smooth collaboration and high project quality.

---

## Group Roles
* **Product Owner: Htoo Aung Kyaw**
* **Scrum Master: ZinMohMohZaw**
* **Developer: Thoon Thiri Han**
* **Developer:Aung Kyaw Zaw**
* **Developer:Cherry Wai Hlaing**
* **Developer: Kaung Khant Zaw**

---

## Ethics & Core Values
* **Ethic**
  1. All team members should be treated with respect and professionalism.
  2. Every team member should have a fair opportunity to participate in the project. 
  3. Team members should communicate honestly about their progress, results, and problems throughout the project. 
  4. AI tools should be used responsibly and according to the project requirements. 
  5. Other people’s work should not be presented as an individual’s own work. 
  6. Mistakes, failures, and problems should be reported rather than deliberately hidden. 
  7. Members should not use another person's mistakes to embarrass or humiliate them. 
  8. No member should be excluded from important group discussions or decisions. 
  9. Team Members should not discriminate against or unfairly treat another member. 
  10. No member should deliberately leave their responsibilities to other group members.

* **Integrity**
  1. Be honest when completing tasks, reporting progress, and communicating with others.
  2. Avoid plagiarism, cheating, falsifying information, or presenting another person's work as their own. 
  3. Take responsibility for their own decisions, actions, and mistakes. 
  4. Provide accurate information and avoid deliberately misleading other members. 
  5. Give credit to team members for their contributions and avoid taking credit for work completed by others. 
  6. Raise concerns when dishonest or unethical behaviour is identified. 
  7. Maintain integrity even when working under pressure or when mistakes could result in negative consequences. 
  8. Follow agreed rules, policies, and ethical standards when carrying out work


---

## Team Responsibility & Conduct
1. Members should complete their assigned tasks within agreed deadlines.
   * If any personal cases (personal cases must be reasonable and understandable as well) occur, please notify ahead. 
   * Keeping the project moving forward is the first priority. 
2. Members should inform the group early when they cannot complete an assigned task. 
3. Members should take responsibility for the quality, scope, and completion of their own task. 
4. Members should attend every scheduled meeting (Exceptions may occur- members cannot participate in meetings when their contribution is not relevant to the discussion)
   * If any personal cases (must be reasonable and understandable as well) occur and certain that you cannot attend, please notify ahead. 
   * So that the meeting can be rescheduled and others can continue their work as well. 
5. Uninformed absence or not attending the meeting for unacceptable amounts of time or for unacceptable reasons will lead to penalties like mark reduction (which will be greatly affecting both your and the group’s final score)
6. Members should be aware of important decisions and responsibilities discussed during meetings. 
7. Members should communicate beforehand before making decisions that could significantly affect another member’s assigned tasks or day-to-day routines. 
8. Members should make reasonable efforts to keep the overall project progressing.

---

## Social & Communication Guidelines
1. No verbal, physical, or written abuse or harassment. 
2. Team members should not discriminate against or unfairly treat another member based on any matters- background, gender, race, religion, or ability. 
3. No bullying, intimidation, or personal attacks. 
4. Communicate respectfully, even when opinions or technical approaches differ. 
5. Negotiation is a must when team members have different ideas or approaches. 
6. Listen to other members and give everyone a chance to contribute. 
7. Feel free to seek help when facing technical or personal difficulties. 
8. Give constructive feedback rather than blaming or criticizing others. 
9. Avoid unnecessary arguments and focus on solving the problem. 
10. Keep team discussions and decisions clear and professional. 
11. Respect other members’ time, responsibilities, and contributions. 
12. Report serious issues to the team Scrum master/Product owner instead of escalating conflicts.


---

## Development Standards
1. Write clean, readable, and well-structured code. 
2. Use meaningful names for variables, methods, and classes. 
3. Test code before committing changes. 
4. Use Git/GitHub for version control. 
5. Make small, clear commits with meaningful messages. 
6. Do not include passwords or sensitive information within code lines. 
7. Review code before merging where possible. 
8. Report and fix bugs and issues clearly.

---

## Version Control & Git Workflow

### Branching Strategy (GitFlow)
To keep the codebase stable and track deployment readiness, the repository strictly follows the GitFlow branching model:

| Branch | Description | Direct Commit Allowed? |
| :--- | :--- | :---: |
| `master` | Production-ready code only. Deployments run exclusively from here. | ❌ Strictly Prohibited |
| `develop` | Primary integration branch for ongoing development. | ❌ Require PR |
| `feature/*` | Dedicated branches for individual tasks or components (e.g., `feature/user-auth`, `feature/docker-setup`). Created off `develop` and merged back into `develop`. | Work Here |
| `release/*` | Prepared for final testing and release tagging (e.g., `release/v0.1.0`). Merged into both `master` and `develop`. | ⚠️ Testing/Fixes Only |
| `hotfix/*` | Urgent fixes applied directly against `master` for critical production issues, then backported to `develop`. | ⚠️ Emergency Only |

---

### Commit Message Standards
Commit messages must be concise, structured, and informative to maintain an auditable commit history.

**Format:**
`<type>(<scope>): <short description>`

#### Allowed Commit Types
* `feat`: A new feature or component.
* `fix`: A bug fix.
* `docs`: Documentation changes only (e.g., updating `README.md`).
* `style`: Formatting, missing semi-colons, etc.; no production code change.
* `refactor`: Refactoring code without changing external behavior.
* `test`: Adding or updating unit/integration tests.
* `chore`: Updating build tasks, package manager configs, or CI dependencies.

> **Example:** `feat(docker): add multi-stage build support for app container`

---

### Pull Request (PR) & Code Review Process

1. **Branch Naming:** Name feature branches clearly according to their purpose (e.g., `feature/login-api` or `fix/db-connection`).
2. **PR Requirements:**
    * Provide a clear PR title and detailed description outlining what was added or changed and how to test it manually.
    * All automated Continuous Integration (CI) checks and unit tests must pass before requesting a review.
    * Require at least **one peer review approval** before merging into `develop` or `master`.
3. **Merge Rules:**
    * Use **Squash and Merge** for feature branches to keep the `develop` commit history clean.
    * Use **Merge Commit** when preserving milestone history for release branches.
    * **Force Push Prohibited:** Never execute `git push --force` on shared branches (`master`, `develop`, or active `release/*` branches).

---

### Secret Management & File Safety
* **`.gitignore` Enforcement:** Every project component must maintain a tracked `.gitignore` file to prevent committing local build artifacts, environment configuration files, or logs (e.g., `target/`, `node_modules/`, `.env`, `.idea/`, `.mvn/`).
* **Zero Hardcoded Credentials:** Never commit passwords, API keys, database URLs, or security tokens to Git repositories. Use environment variable templates (e.g., `.env.example`) for local developer setup guidelines.

---
# Code of Conduct Markings and Task Penalty System

## 1. Code of Conduct Marking

Each group member shall initially receive **16.7 marks**[cite: 1]. Penalties shall be deducted when a member violates the agreed Code of Conduct[cite: 1].

The values below represent the maximum deductions available for each category, rather than automatically deducting the entire amount for every violation[cite: 1].

| Category | Maximum Deduction | Priority |
| :--- | :---: | :---: |
| **Integrity** | 4.0 | Very High |
| **Ethics** | 3.0 | High |
| **Responsibility** | 3.0 | High |
| **Development Standards** | 2.5 | Medium |
| **Version Control & Git Workflow** | 2.2 | Medium |
| **Social & Communication** | 2.0 | Medium |
| **Total** | **16.7** | |

---

## 2. Violation Severity & Penalty Scale

The penalty shall be determined according to the category and severity of the violation[cite: 1]. Minor, major, and severe violations result in deductions of **25%**, **50%**, and **100%** of the respective category's allocated marks[cite: 1].

* **Minor Violation (25% Deduction):** Small mistakes that have limited impact on the project, particularly if corrected promptly[cite: 1].
* **Major Violation (50% Deduction):** Repeated violations, failure to complete assigned responsibilities, or behavior that negatively affects the team's progress[cite: 1].
* **Severe Violation (100% Deduction):** Deliberate dishonesty, serious ethical misconduct, or actions that significantly damage the team's work or trust[cite: 1].

### Penalty Deduction Matrix

| Category | Minor (25%) | Major (50%) | Severe (100%) |
| :--- | :---: | :---: | :---: |
| **Integrity** | 1.00 | 2.00 | 4.00 |
| **Ethics** | 0.75 | 1.50 | 3.00 |
| **Responsibility** | 0.75 | 1.50 | 3.00 |
| **Development Standards** | 0.625 | 1.25 | 2.50 |
| **Version Control & Git Workflow** | 0.55 | 1.10 | 2.20 |
| **Social & Communication** | 0.50 | 1.00 | 2.00 |

### Penalty and Disciplinary System Rules
* **Evidence & Fairness:** All violations must be supported by reasonable evidence and reviewed fairly[cite: 1]. Members shall have an opportunity to explain their actions before a penalty is finalized[cite: 1].
* **Capped Deductions:** Repeated violations may result in additional deductions, provided that the total deduction within each category does not exceed its allocated maximum[cite: 1].
* **No Double Jeopardy:** The same incident shall not be penalized under multiple categories unless it involves clearly separate violations[cite: 1].
* **Transparency:** All penalty decisions shall be documented and communicated transparently[cite: 1].
* **Floor Limit & Escalation:** A member's final marks shall not fall below zero[cite: 1]. Serious misconduct may additionally be referred to the lecturer in accordance with institutional regulations[cite: 1].

---

## 3. Task Penalty System

Penalties apply per violation rather than a fixed amount deducted from everyone[cite: 1]. A member who completes all assigned tasks correctly and on time retains their full 16.7 marks[cite: 1].

| No. | Category / Breach | Penalty |
| :---: | :--- | :---: |
| 1 | Not completing an assigned task | −2.0 |
| 2 | Implementing incorrect or unassigned functionality | −1.5 |
| 3 | Handing over a task without making reasonable attempts to resolve errors | −1.0 |
| 4 | Failing to complete a task within the agreed deadline | −1.0 |
| 5 | Failing to test or verify the implemented functionality | −0.5 |
| 6 | Failing to inform the group about issues that may delay task completion | −0.5 |
| 7 | Failing to provide a proper handover of unfinished work | −0.5 |

### Detailed Conditions for Each Deduction

| Category | Condition | Deduction |
| :--- | :--- | :---: |
| **Task Completion** | Partially completed task | −1.0 |
| | No usable implementation | −2.0 |
| **Correct Implementation** | Function implemented with major errors | −1.0 |
| | Completely incorrect or unrelated functionality | −1.5 |
| **Problem-Solving** | Hands over the task without making reasonable efforts to resolve an error | −1.0 |
| **Deadline Compliance** | Misses the agreed deadline without an approved extension | −1.0 |
| **Testing** | Submits functionality without performing agreed tests | −0.5 |
| **Communication** | Fails to report significant blockers promptly | −0.5 |
| **Handover** | Fails to provide code, progress information, or relevant error details during reassignment | −0.5 |

> **Note:** A minor coding error should not automatically result in a deduction if the member corrects it through the normal review process[cite: 1].

---

## 4. Task Takeover and Mark Transfer

When a member fails to complete an assigned task and another member takes over, the original member loses the corresponding task completion marks[cite: 1]. These marks are transferred to the member who completes the remaining work based on actual effort performed[cite: 1].

| Takeover Situation | Marks Transferred |
| :--- | :---: |
| Remaining minor corrections or fixes | 0.5 |
| Significant unfinished functionality | 1.0 |
| Majority of the assigned task remains unfinished | 1.5 |
| Entire task must be completed by another member | 2.0 |

---

## 5. Overriding Rules & Guidelines

* **Rule 1: Approved Extensions:** No deadline penalty if the group approves an extension before the deadline[cite: 1].
* **Rule 2: Asking for Help:** Members are encouraged to request assistance[cite: 1]. Penalties apply to abandoning responsibilities without reasonable effort, not to collaboration[cite: 1].
* **Rule 3: Evidence Requirement:** Penalties must be supported by task records, GitHub commits, pull requests, or other relevant evidence[cite: 1].
* **Rule 4: No Duplicate Penalties:** The same violation should not be penalized twice under both the Task Penalty System and the Code of Conduct[cite: 1].