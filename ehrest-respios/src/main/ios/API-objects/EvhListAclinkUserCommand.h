//
// EvhListAclinkUserCommand.h
// generated at 2016-04-07 17:57:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAclinkUserCommand
//
@interface EvhListAclinkUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

