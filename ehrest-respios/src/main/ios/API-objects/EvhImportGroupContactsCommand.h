//
// EvhImportGroupContactsCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImportGroupContactsCommand
//
@interface EvhImportGroupContactsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

