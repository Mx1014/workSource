//
// EvhAclinkUserDTO.h
// generated at 2016-04-07 17:57:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUserDTO
//
@interface EvhAclinkUserDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* authId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

