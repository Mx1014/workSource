//
// EvhListGroupsByNamespaceIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupsByNamespaceIdCommand
//
@interface EvhListGroupsByNamespaceIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* privateFlag;

@property(nonatomic, copy) NSString* keywords;

@property(nonatomic, copy) NSNumber* categoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

