//
// EvhUserInfoFroBiz.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInfoFroBiz
//
@interface EvhUserInfoFroBiz
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* namespaceUserToken;

@property(nonatomic, copy) NSString* telePhone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

