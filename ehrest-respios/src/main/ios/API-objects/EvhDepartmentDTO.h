//
// EvhDepartmentDTO.h
// generated at 2016-03-31 10:18:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDepartmentDTO
//
@interface EvhDepartmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* departmentName;

@property(nonatomic, copy) NSString* superiorDepartment;

@property(nonatomic, copy) NSString* departmentType;

@property(nonatomic, copy) NSString* role;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

