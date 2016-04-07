//
// EvhGroupByNamespaceIdActionData.h
// generated at 2016-04-07 14:16:29 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupByNamespaceIdActionData
//
@interface EvhGroupByNamespaceIdActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* privateFlag;

@property(nonatomic, copy) NSString* keywords;

@property(nonatomic, copy) NSNumber* categoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

