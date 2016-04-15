//
// EvhListQualityStandardsCommand.h
// generated at 2016-04-12 15:02:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityStandardsCommand
//
@interface EvhListQualityStandardsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

