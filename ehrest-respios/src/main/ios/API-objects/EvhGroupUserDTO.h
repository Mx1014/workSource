//
// EvhGroupUserDTO.h
// generated at 2016-04-19 13:40:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupUserDTO
//
@interface EvhGroupUserDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* operatorType;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* employeeNo;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

