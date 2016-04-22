//
// EvhConfOrderAccountDTO.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfOrderAccountDTO
//
@interface EvhConfOrderAccountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* departmentId;

@property(nonatomic, copy) NSString* department;

@property(nonatomic, copy) NSNumber* confType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

