#!/bin/zsh
set -eu

# Copyright 2013 University of Chicago and Argonne National Laboratory
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License

# TURBINE-SLURM-RUN
# Creates a SLURM run file and runs it on the given program

# USAGE
# > VAR1=VALUE1 VAR2=VALUE2 turbine-slurm-run.zsh <PROGRAM> <ARGS>*

# ENVIRONMENT
# TODO: User input should be PROCS, not NODES (#648)
# NODES: Number of nodes to use
# PPN:   Processes-per-node

print "TURBINE-SLURM"

export TURBINE_HOME=$( cd "$(dirname "$0")/../../.." ; /bin/pwd )
source ${TURBINE_HOME}/scripts/submit/run-init.zsh
if [[ ${?} != 0 ]]
then
  print "Broken Turbine installation!"
  declare TURBINE_HOME
  return 1
fi
declare TURBINE_HOME


print run-init done
checkvars PROGRAM NODES PPN
export    PROGRAM NODES PPN

TURBINE_SLURM_M4=${TURBINE_HOME}/scripts/submit/slurm/turbine-slurm.sh.m4
TURBINE_SLURM=${TURBINE_OUTPUT}/turbine-slurm.sh

m4 ${TURBINE_SLURM_M4} > ${TURBINE_SLURM}

print "wrote: ${TURBINE_SLURM}"

Q=""
if (( ${+QUEUE} ))
then
  Q="--partition=${QUEUE}"
fi

set -x
sbatch --exclusive --constraint=ib \
  --output=${OUTPUT_FILE}          \
  --error=${OUTPUT_FILE}           \
  ${Q}                             \
  --job-name=${TURBINE_JOBNAME}    \
  ${TURBINE_SLURM} ${PROGRAM} ${ARGS} | read __ __ __ JOB_ID

# JOB_ID must be an integer:
if [[ ${JOB_ID} == "" || ${JOB_ID} != <-> ]]
then
  abort "sbatch failed!"
fi
declare JOB_ID

# Fill in turbine.log
turbine_log >> ${LOG_FILE}
# Fill in jobid.txt
print ${JOB_ID} > ${JOB_ID_FILE}

return 0
