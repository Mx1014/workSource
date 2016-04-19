//
// EvhListWebMenuPrivilegeDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhWebMenuPrivilegeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuPrivilegeDTO
//
@interface EvhListWebMenuPrivilegeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* moduleId;

@property(nonatomic, copy) NSString* moduleName;

// item type EvhWebMenuPrivilegeDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

