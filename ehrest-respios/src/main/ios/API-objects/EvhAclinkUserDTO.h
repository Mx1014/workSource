//
// EvhAclinkUserDTO.h
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

