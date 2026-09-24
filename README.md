
# World Population Reporting System

[![Master Build Status](https://img.shields.io/github/actions/workflow/status/ZinMohMohZaw/G5-DevOps/build.yml?branch=master)](https://github.com/ZinMohMohZaw/G5-DevOps/actions/workflows/build.yml)
[![Develop Build Status](https://img.shields.io/github/actions/workflow/status/ZinMohMohZaw/G5-DevOps/build.yml?branch=develop)](https://github.com/ZinMohMohZaw/G5-DevOps/actions/workflows/build.yml)
[![Code Coverage](https://img.shields.io/badge/coverage-0%25-red.svg)](https://github.com/<ZinMohMohZaw>/<G5-DevOps>)
[![Release](https://img.shields.io/github/v/release/<ZinMohMohZaw>/<G5-DevOps>?include_prereleases)](https://github.com/<ZinMohMohZaw>/<G5-DevOps>/releases)
[![License](https://img.shields.io/github/license/<ZinMohMohZaw>/<G5-DevOps>)](LICENSE)
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