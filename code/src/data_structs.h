/*
  Support for struct data type in ADLB
 */

#ifndef __XLB_DATA_STRUCTS_H
#define __XLB_DATA_STRUCTS_H

#include "adlb-defs.h"
#include "adlb_types.h"
#include "data.h"
#include "notifications.h"

/*
  Type of a struct field.
  Need extra type info because we might need to initialize the data
  item, e.g. if a subscript of a field is assigned.  If extra type
  info is required to initialize the type, and it is not provided,
  any operation that requires initializing the field will fail.
  Currently the extra type info is only used to initialize fields:
  it is not used to type-check assignments to fields.
 */
typedef struct {
  bool initialized;
  char *type_name;
  int field_count;
  char **field_names;
  adlb_struct_field_type *field_types;
} xlb_struct_type_info;

adlb_data_code xlb_new_struct(adlb_struct_type type, adlb_struct **s);

// Free all memory allocated within this module
adlb_data_code xlb_struct_finalize(void);

// Return the name of a declared struct type
const char *xlb_struct_type_name(adlb_struct_type type);

// Return info about the struct type
// return: pointer to struct type info, valid until finalize called.
//         NULL if invalid type
const xlb_struct_type_info *
xlb_get_struct_type_info(adlb_struct_type type);

// Free memory associated with struct, including the
// provided pointer if specified
adlb_data_code xlb_free_struct(adlb_struct *s, bool free_root_ptr);

/*
 * Get data for struct field.
 * Returns error if field invalid.
 * val: result if field is set, NULL if not yet set
 * type: type of field (always set)
 */
adlb_data_code xlb_struct_get_field(adlb_struct *s, int field_ix,
                        const adlb_datum_storage **val, adlb_data_type *type);

adlb_data_code xlb_struct_get_subscript(adlb_struct *s, adlb_subscript subscript,
                        const adlb_datum_storage **val, adlb_data_type *type);

adlb_data_code xlb_struct_subscript_init(adlb_struct *s, adlb_subscript subscript,
                                        bool *b);

// Get data for struct field
adlb_data_code xlb_struct_set_field(adlb_struct *s, int field_ix,
                        const void *data, int length, adlb_data_type type);

adlb_data_code xlb_struct_set_subscript(adlb_struct *s, adlb_subscript subscript,
                        const void *data, int length, adlb_data_type type);

adlb_data_code
xlb_struct_cleanup(adlb_struct *s, bool free_mem, bool release_read,
                   bool release_write, 
                   xlb_acquire_rc to_acquire,
                   xlb_rc_changes *rc_changes);

char *xlb_struct_repr(adlb_struct *s);

// Convert subscript to struct field ID
adlb_data_code xlb_struct_str_to_ix(adlb_subscript subscript, int *field_ix);

#endif // __XLB_DATA_STRUCTS_H
