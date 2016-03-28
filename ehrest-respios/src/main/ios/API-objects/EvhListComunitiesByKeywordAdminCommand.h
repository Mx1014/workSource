//
// EvhListComunitiesByKeywordAdminCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListComunitiesByKeywordAdminCommand
//
@interface EvhListComunitiesByKeywordAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

