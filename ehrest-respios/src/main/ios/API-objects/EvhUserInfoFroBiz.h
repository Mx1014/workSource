//
// EvhUserInfoFroBiz.h
// generated at 2016-03-25 11:43:34 
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

