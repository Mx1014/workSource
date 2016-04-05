//
// EvhGroupByNamespaceIdActionData.h
// generated at 2016-04-05 13:45:26 
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

