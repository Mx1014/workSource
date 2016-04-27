//
// EvhListVideoConfAccountConfRecordCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountConfRecordCommand
//
@interface EvhListVideoConfAccountConfRecordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accountId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

