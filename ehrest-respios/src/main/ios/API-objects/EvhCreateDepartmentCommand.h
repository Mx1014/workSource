//
// EvhCreateDepartmentCommand.h
// generated at 2016-03-31 11:07:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDepartmentCommand
//
@interface EvhCreateDepartmentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* departmentName;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* departmentType;

@property(nonatomic, copy) NSNumber* roleId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

